package com.blu.credit.conclusion.integration.flow;

import static org.springframework.integration.http.HttpHeaders.STATUS_CODE;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.blu.credit.conclusion.integration.config.channel.MyChannels;
import com.blu.credit.conclusion.model.CreditConclusion;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class CreditConclusionFlow {

    private final MyChannels myChannels;

    @Bean
    IntegrationFlow successFlow() {
        return IntegrationFlows
            .from(myChannels.successCreditConclusionChannel())
            .log(Level.INFO, message -> "***** Congratulation, " + getApplicantName(message) + "! You has got a loan!")
            .handle((payload, headers) ->
                MessageBuilder
                    .withPayload(payload)
                    .copyHeaders(headers)
                    .setHeader(STATUS_CODE, HttpServletResponse.SC_OK)
            )
            .get();
    }

    @Bean
    IntegrationFlow refuseFlow() {
        return IntegrationFlows
            .from(myChannels.refusedCreditConclusionChannel())
            .log(Level.INFO,
                message -> "***** Sorry, " + getApplicantName(message) + "! Your loan application cannot be satisfied")
            .handle((payload, headers) ->
                MessageBuilder
                    .withPayload(payload)
                    .copyHeaders(headers)
                    .setHeader(STATUS_CODE, HttpServletResponse.SC_BAD_REQUEST)
            )
            .get();
    }

    private String getApplicantName(final Message<Object> message) {
        return ((CreditConclusion) message.getPayload()).getApplicant().getName();
    }
}
