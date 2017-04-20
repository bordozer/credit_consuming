package com.blu.integration.coordination.flow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.client.RestTemplate;

import com.blu.integration.coordination.config.channel.MyChannels;
import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PoliceFlows {

    private final MyChannels myChannels;
    private final RestTemplate restTemplate;

    @Value("${cliento.url}")
    private String clientoUrl;

    @Bean
    public IntegrationFlow clientoFlow() {
        return IntegrationFlows
            .from(myChannels.policeChannel())
            .log(Level.INFO, message -> "***** Validating cliento " + message.getPayload())
            .handle(clientoHandler())
            .get();
    }

    @Bean
    MessageHandler clientoHandler() {
        return Http.outboundGateway(clientoUrl, restTemplate)
            .expectedResponseType(PoliceResponse.class)
            .httpMethod(HttpMethod.POST)
            .get();
    }
}
