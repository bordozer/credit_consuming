package com.blu.credit.conclusion.integration.processors;

import java.util.Map;

import org.springframework.util.Assert;

import com.blu.credit.conclusion.model.CreditConclusion;
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
        Assert.isTrue(response.size() > 0, "No police response! It must be a holiday there...");
        return response.values().stream()
            .filter(resp -> ClientType.CRIMINAL.equals(resp.getClientType()))
            .findAny()
            .map(resp -> CreditConclusion.refuse(resp.getApplicant()))
            .orElseGet(() -> downStreamProcessor.process(response));
    }
}
