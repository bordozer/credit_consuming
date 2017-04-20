package com.blu.integration.coordination.config.transform;

import static com.blu.integration.coordination.flow.CoordinationFlows.HEADER_APPLICANT_TYPE;

import java.util.Arrays;
import java.util.List;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;

import com.blu.integration.coordination.dto.ApplicantsData;
import com.blu.integration.model.Applicant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicantTransformer implements GenericTransformer<Message<ApplicantsData>, List<Message<Applicant>>> {

    @Override
    public List<Message<Applicant>> transform(final Message<ApplicantsData> source) {
        return Arrays.asList(
            createMessage(source.getPayload().getApplicant(), "Applicant"),
            createMessage(source.getPayload().getCoApplicant(), "CoApplicant")
        );
    }

    private Message<Applicant> createMessage(final Applicant applicant, final String applicantType) {
        log.info("***** Extracting applicant: {}", applicant);
        return MessageBuilder
            .withPayload(applicant)
            .setHeader(HEADER_APPLICANT_TYPE, applicantType)
            .build();
    }
}
