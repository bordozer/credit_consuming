package com.blu.credit.conclusion.reactor.services;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;

public interface BankFlowService {

    CreditConclusion getCreditConclusion(ApplicantsData applicants);
}
