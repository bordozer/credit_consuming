package com.blu.integration.coordination.processors;

import java.util.Map;

import com.blu.integration.coordination.model.CreditConclusion;
import com.blu.integration.model.ClientoType;
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
            .filter(resp -> ClientoType.CRIMINAL.equals(resp.getClientoType()))
            .findAny()
            .map(resp -> CreditConclusion.refuse(resp.getApplicant()))
            .orElseGet(() -> downStreamProcessor.process(response));
    }
}
