package com.blu.credit.conclusion.reactor.services;

import org.springframework.stereotype.Service;

import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoliceServiceImpl implements PoliceService {

    @Override
    public Mono<PoliceResponse> process(final Applicant applicant) {
        return Mono.fromCallable(() -> buildPoliceResponse(applicant));
    }

    private PoliceResponse buildPoliceResponse(final Applicant applicant) {
        return PoliceResponse.respectable(applicant);
    }
}
