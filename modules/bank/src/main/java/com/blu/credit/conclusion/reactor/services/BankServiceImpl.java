package com.blu.credit.conclusion.reactor.services;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.credit.conclusion.reactor.exception.ApplicantIsCriminalException;
import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;
import com.google.common.collect.ImmutableList;

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
        log.info("***** Credit inquiry {}", applicants);
        return Flux.concat(getApplicantPromises(applicants))
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

    private List<Mono<PoliceResponse>> getApplicantPromises(final ApplicantsData applicants) {
        final Applicant applicant = applicants.getApplicant();
        Assert.notNull(applicant, "Applicant cannot be null");

        final Applicant coApplicant = applicants.getCoApplicant();

        if (coApplicant == null) {
            return Collections.singletonList(getPoliceResponsePromise(applicant));
        }

        return ImmutableList.of(getPoliceResponsePromise(applicant), getPoliceResponsePromise(coApplicant));
    }

    private Mono<CreditConclusion> processPositivePoliceResponses(final List<PoliceResponse> policeResponses) {
        final Applicant applicant = policeResponses.get(0).getApplicant();
        return Mono.just(CreditConclusion.loan(applicant));
    }

    private Mono<? extends CreditConclusion> processNegativePoliceResponses(final Applicant criminal) {
        return Mono.just(CreditConclusion.refuse(criminal));
    }

    private Mono<PoliceResponse> getPoliceResponsePromise(final Applicant applicant) {
        return policeService.process(applicant);
    }
}
