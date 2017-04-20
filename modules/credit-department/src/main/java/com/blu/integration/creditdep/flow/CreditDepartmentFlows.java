package com.blu.integration.creditdep.flow;

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
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;

import com.blu.integration.creditdep.aggregate.PoliceResponseAggregator;
import com.blu.integration.creditdep.aggregate.PoliceResponseAggregatorReleaseStrategy;
import com.blu.integration.creditdep.config.channel.MyChannels;
import com.blu.integration.creditdep.config.transform.ApplicantTransformer;
import com.blu.integration.creditdep.dto.ApplicantsData;
import com.blu.integration.creditdep.model.CreditConclusion;
import com.blu.integration.creditdep.model.CreditConclusionAction;
import com.blu.integration.creditdep.processors.CriminalClientResponseProcessor;
import com.blu.integration.creditdep.processors.PoliceResponseProcessor;
import com.blu.integration.creditdep.processors.RespectableClientResponseProcessor;
import com.blu.integration.model.Applicant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CreditDepartmentFlows {

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
