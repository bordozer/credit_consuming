package com.blu.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliceResponse {
    private Applicant applicant;
    private ClientoType clientoType;

    public static PoliceResponse respectable(final Applicant applicant) {
        return new PoliceResponse(applicant, ClientoType.RESPECTABLE);
    }

    public static PoliceResponse criminal(final Applicant applicant) {
        return new PoliceResponse(applicant, ClientoType.CRIMINAL);
    }
}
