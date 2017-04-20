package com.blu.integration.creditdep.processors;

import java.util.Map;

import com.blu.integration.creditdep.model.CreditConclusion;
import com.blu.integration.model.PoliceResponse;

public interface PoliceResponseProcessor {

    CreditConclusion process(Map<String, PoliceResponse> response);
}
