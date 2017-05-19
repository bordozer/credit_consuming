package com.blu.credit.conclusion.reactor.services;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;

import reactor.core.publisher.Mono;

public interface BankFlowService {

    Mono<CreditConclusion> getCreditConclusion(ApplicantsData applicants);
}
