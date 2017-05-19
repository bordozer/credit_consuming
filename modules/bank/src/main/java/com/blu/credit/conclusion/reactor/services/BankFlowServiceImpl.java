package com.blu.credit.conclusion.reactor.services;

import org.springframework.stereotype.Service;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BankFlowServiceImpl implements BankFlowService {

    @Override
    public Mono<CreditConclusion> getCreditConclusion(final ApplicantsData applicants) {
        log.info("***** Inquery {}", applicants);
        final CreditConclusion loan = CreditConclusion.loan(applicants.getApplicant());
        return Mono.just(loan);
    }
}
