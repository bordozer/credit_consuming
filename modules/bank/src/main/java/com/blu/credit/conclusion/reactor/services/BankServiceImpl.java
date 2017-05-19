package com.blu.credit.conclusion.reactor.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.credit.conclusion.reactor.exception.ApplicantIsCriminalException;
import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final PoliceService policeService;

    @Override
    public Mono<CreditConclusion> process(final ApplicantsData applicants) {
        log.info("***** Inquery {}", applicants);

        final Applicant applicant = applicants.getApplicant();
        Assert.notNull(applicant, "Applicant cannot be null");

        final Applicant coApplicant = applicants.getCoApplicant();

        final Mono<PoliceResponse> applicantPoliceResponsePromise = getPoliceResponse(applicant);
        final Mono<PoliceResponse> coApplicantPoliceResponsePromise = getPoliceResponse(coApplicant);

        return Flux.concat(applicantPoliceResponsePromise, coApplicantPoliceResponsePromise)
            .collectList()
            .then(this::processPositivePoliceResponses)
            .onErrorResume(e -> {
                if (e instanceof ApplicantIsCriminalException) {
                    final Applicant criminal = ((ApplicantIsCriminalException) e).getApplicant();
                    return processNegativePoliceResponses(criminal);
                }
                throw new RuntimeException("Something went wrong...");
            });
    }

    private Mono<CreditConclusion> processPositivePoliceResponses(final List<PoliceResponse> policeResponses) {
        final Applicant applicant = policeResponses.get(0).getApplicant();
        return confirmLoan(applicant);
    }

    private Mono<? extends CreditConclusion> processNegativePoliceResponses(final Applicant criminal) {
        return refuseLoan(criminal);
    }

    private Mono<CreditConclusion> confirmLoan(final Applicant applicant) {
        return Mono.just(CreditConclusion.loan(applicant));
    }

    private Mono<CreditConclusion> refuseLoan(final Applicant applicant) {
        return Mono.just(CreditConclusion.refuse(applicant));
    }

    private Mono<PoliceResponse> getPoliceResponse(final Applicant applicant) {
        return policeService.process(applicant);
    }
}
