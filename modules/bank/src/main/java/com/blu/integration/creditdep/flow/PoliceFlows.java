package com.blu.integration.creditdep.flow;

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

import com.blu.integration.creditdep.config.channel.MyChannels;
import com.blu.integration.model.PoliceResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PoliceFlows {

    private final MyChannels myChannels;
    private final RestTemplate restTemplate;

    @Value("${police.url}")
    private String policeUrl;

    @Bean
    public IntegrationFlow policeFlow() {
        return IntegrationFlows
            .from(myChannels.policeChannel())
            .log(Level.INFO, message -> "^^^^^ Getting info from police about " + message.getPayload())
            .handle(policeHandler())
            .get();
    }

    @Bean
    MessageHandler policeHandler() {
        return Http.outboundGateway(policeUrl, restTemplate)
            .expectedResponseType(PoliceResponse.class)
            .httpMethod(HttpMethod.POST)
            .get();
    }
}
