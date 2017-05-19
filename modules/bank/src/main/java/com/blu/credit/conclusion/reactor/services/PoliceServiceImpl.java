package com.blu.credit.conclusion.reactor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            return Mono.empty();
        }
        return Mono.fromCallable(() -> getPoliceResponse(applicant));
    }

    private PoliceResponse getPoliceResponse(final Applicant applicant) {
        final PoliceResponse policeResponse = doPoliceResponse(applicant);
        if (ClientType.CRIMINAL.equals(policeResponse.getClientType())) {
            return PoliceResponse.criminal(applicant);
        }
        return PoliceResponse.respectable(applicant);
    }

    private PoliceResponse doPoliceResponse(final Applicant applicant) {
        return restTemplate.postForObject(policeUrl, applicant, PoliceResponse.class);
    }
}
