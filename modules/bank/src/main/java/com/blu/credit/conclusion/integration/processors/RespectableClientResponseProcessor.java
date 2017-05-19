package com.blu.credit.conclusion.integration.processors;

import java.util.Map;

import org.springframework.util.Assert;

import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.integration.model.PoliceResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RespectableClientResponseProcessor implements PoliceResponseProcessor {

    @Override
    public CreditConclusion process(final Map<String, PoliceResponse> response) {
        Assert.isTrue(response.size() > 0, "No police response! It must be a holiday there...");
        return CreditConclusion.loan(response.entrySet().iterator().next().getValue().getApplicant());
    }
}
