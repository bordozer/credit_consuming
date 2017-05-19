package com.blu.credit.conclusion.model;

import com.blu.integration.model.Applicant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class CreditConclusion {

    private final Applicant applicant;
    private final CreditConclusionAction creditAction;

    public static CreditConclusion loan(Applicant applicant) {
        log.info("***** {} is a respectable client. Loan application satisfied", applicant);
        return new CreditConclusion(applicant, CreditConclusionAction.CREDIT_CONFIRMED);
    }

    public static CreditConclusion refuse(Applicant applicant) {
        log.info("***** {} is a respectable client. Loan application refused", applicant);
        return new CreditConclusion(applicant, CreditConclusionAction.REFUSE);
    }
}
