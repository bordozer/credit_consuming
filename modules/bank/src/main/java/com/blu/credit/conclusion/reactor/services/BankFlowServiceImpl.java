package com.blu.credit.conclusion.reactor.services;

import org.springframework.stereotype.Service;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;

@Service
public class BankFlowServiceImpl implements BankFlowService {

    @Override
    public CreditConclusion getCreditConclusion(final ApplicantsData applicants) {
        return CreditConclusion.loan(applicants.getApplicant());
    }
}
