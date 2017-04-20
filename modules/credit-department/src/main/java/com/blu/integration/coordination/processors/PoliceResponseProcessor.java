package com.blu.integration.coordination.processors;

import java.util.Map;

import com.blu.integration.coordination.model.CreditConclusion;
import com.blu.integration.model.PoliceResponse;

public interface PoliceResponseProcessor {

    CreditConclusion process(Map<String, PoliceResponse> response);
}
