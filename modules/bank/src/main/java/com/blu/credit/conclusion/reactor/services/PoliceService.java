package com.blu.credit.conclusion.reactor.services;

import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;

import reactor.core.publisher.Mono;

public interface PoliceService {

    Mono<PoliceResponse> process(Applicant applicant);
}
