package com.blu.integration.creditdep.processors;

import java.util.Map;

import com.blu.integration.creditdep.model.CreditConclusion;
import com.blu.integration.model.ClientType;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CriminalClientResponseProcessor implements PoliceResponseProcessor {

    private final PoliceResponseProcessor downStreamProcessor;

    @Override
    public CreditConclusion process(final Map<String, PoliceResponse> response) {
        return response.values().stream()
            .filter(resp -> ClientType.CRIMINAL.equals(resp.getClientType()))
            .findAny()
            .map(resp -> CreditConclusion.refuse(resp.getApplicant()))
            .orElseGet(() -> downStreamProcessor.process(response));
    }
}
