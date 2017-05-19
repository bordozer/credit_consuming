package com.blu.credit.conclusion.integration.processors;

import java.util.Map;

import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.integration.model.PoliceResponse;

public interface PoliceResponseProcessor {

    CreditConclusion process(Map<String, PoliceResponse> response);
}
