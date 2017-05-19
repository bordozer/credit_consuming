package com.blu.credit.conclusion.reactor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blu.credit.conclusion.reactor.exception.ApplicantIsCriminalException;
import com.blu.integration.model.Applicant;
import com.blu.integration.model.ClientType;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoliceServiceImpl implements PoliceService {

    private final RestTemplate restTemplate;

    @Value("${police.url}")
    private String policeUrl;

    @Override
    public Mono<PoliceResponse> process(final Applicant applicant) {
        if (applicant == null) {
            log.info("***** Applicant is null. Do not disturb the Police for nothing.");
            return Mono.empty();
        }
        return Mono.fromCallable(() -> getPoliceResponse(applicant));
    }

    private PoliceResponse getPoliceResponse(final Applicant applicant) {
        final PoliceResponse policeResponse = doPoliceResponse(applicant);
        if (ClientType.CRIMINAL.equals(policeResponse.getClientType())) {
            throw new ApplicantIsCriminalException(applicant);
        }
        return PoliceResponse.respectable(applicant);
    }

    private PoliceResponse doPoliceResponse(final Applicant applicant) {
        log.info("***** Sending request to police about applicant {}", applicant);
        return restTemplate.postForObject(policeUrl, applicant, PoliceResponse.class);
    }
}
