package com.blu.integration.coordination.flow;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;

import com.blu.integration.coordination.aggregate.PoliceResponseAggregator;
import com.blu.integration.coordination.aggregate.PoliceResponseAggregatorReleaseStrategy;
import com.blu.integration.coordination.config.channel.MyChannels;
import com.blu.integration.coordination.config.transform.ApplicantTransformer;
import com.blu.integration.coordination.dto.ApplicantsData;
import com.blu.integration.coordination.model.CreditConclusion;
import com.blu.integration.coordination.model.CreditConclusionAction;
import com.blu.integration.coordination.processors.CriminalClientResponseProcessor;
import com.blu.integration.coordination.processors.PoliceResponseProcessor;
import com.blu.integration.coordination.processors.RespectableClientResponseProcessor;
import com.blu.integration.model.Applicant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CoordinationFlows {

    public static final String HEADER_APPLICANT_TYPE = "ApplicantType";

    private final MyChannels myChannels;

    @Bean
    MessagingGatewaySupport inboundGateway() {
        return Http.inboundGateway("/inbound")
            .requestMapping(requestMapping -> {
                requestMapping.consumes(MediaType.APPLICATION_JSON);
                requestMapping.methods(HttpMethod.POST);
            })
            .requestPayloadType(ApplicantsData.class)
            .get();
    }

    @Bean
    public IntegrationFlow creditFlow() {
        return IntegrationFlows.from(inboundGateway())
            .transform(applicantTransformer())
            .split(spec -> spec.async(true))
            .gateway(myChannels.policeChannel())
            .aggregate(aggregatorSpec -> aggregatorSpec
                .releaseStrategy(policeResponseReleaseStrategy())
                .outputProcessor(policeMessagesAggregator())
            )
            .handle(policeResponseProcessorsChain())
            .<CreditConclusion, CreditConclusionAction>route(CreditConclusion::getCreditAction,
                mapping -> mapping
                    .subFlowMapping(CreditConclusionAction.CREDIT_CONFIRMED,
                        subFlow -> subFlow.gateway(myChannels.successCreditConclusionChannel()))
                    .subFlowMapping(CreditConclusionAction.REFUSE,
                        subFlow -> subFlow.gateway(myChannels.refusedCreditConclusionChannel()))
            )
            .get();
    }

    @Bean
    GenericTransformer<Message<ApplicantsData>, List<Message<Applicant>>> applicantTransformer() {
        return new ApplicantTransformer();
    }

    @Bean
    ReleaseStrategy policeResponseReleaseStrategy() {
        return new PoliceResponseAggregatorReleaseStrategy();
    }

    @Bean
    MessageGroupProcessor policeMessagesAggregator() {
        return new PoliceResponseAggregator(HEADER_APPLICANT_TYPE);
    }

    @Bean
    PoliceResponseProcessor policeResponseProcessorsChain() {
        return new CriminalClientResponseProcessor(new RespectableClientResponseProcessor());
    }
}
