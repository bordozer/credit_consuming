package com.blu.integration.creditdep.processors;

import java.util.Map;

import com.blu.integration.creditdep.model.CreditConclusion;
import com.blu.integration.model.PoliceResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RespectableClientResponseProcessor implements PoliceResponseProcessor {

    @Override
    public CreditConclusion process(final Map<String, PoliceResponse> response) {
        return CreditConclusion.loan(response.entrySet().iterator().next().getValue().getApplicant());
    }
}