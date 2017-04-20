package com.blu.integration.creditdep.dto;

import com.blu.integration.model.Applicant;

import lombok.Data;

@Data
public class ApplicantsData {
    private Applicant applicant;
    private Applicant coApplicant;
}
