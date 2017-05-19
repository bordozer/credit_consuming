package com.blu.credit.conclusion.reactor.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.integration.model.Applicant;
import com.blu.integration.model.ClientType;
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
            .then(this::processPoliceResponses);
    }

    private Mono<CreditConclusion> processPoliceResponses(final List<PoliceResponse> policeResponses) {
        if (policeResponses.size() > 1) {
            final PoliceResponse coApplicantPoliceResponse = policeResponses.get(1);
            if (isCriminal(coApplicantPoliceResponse)) {
                return refuseLoan(coApplicantPoliceResponse.getApplicant());
            }
        }

        final PoliceResponse applicantPoliceResponse = policeResponses.get(0);
        if (isCriminal(applicantPoliceResponse)) {
            return refuseLoan(applicantPoliceResponse.getApplicant());
        }
        return confirmLoan(applicantPoliceResponse.getApplicant());
    }

    private boolean isCriminal(final PoliceResponse coApplicantPoliceResponse) {
        return ClientType.CRIMINAL.equals(coApplicantPoliceResponse.getClientType());
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
