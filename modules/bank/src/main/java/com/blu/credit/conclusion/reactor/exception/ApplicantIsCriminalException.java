package com.blu.credit.conclusion.reactor.exception;

import com.blu.integration.model.Applicant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApplicantIsCriminalException extends RuntimeException {
    private final Applicant applicant;
}
