package com.blu.integration.police;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import org.junit.Rule;
import org.junit.Test;

import com.blu.integration.model.Applicant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import lombok.SneakyThrows;

public class PoliceHappyPathFlowTest {

    private static final String APPLICATION_JSON = "application/json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    @SneakyThrows
    public void exampleTest() {
        /*final Applicant hero = new Applicant("Hero");

        stubFor(post(urlEqualTo("/"))
            .withHeader("Accept", equalTo(APPLICATION_JSON))
            .willReturn(aResponse()
                .withStatus(SC_OK)
                .withHeader("Content-Type", APPLICATION_JSON)
                .withBody(toJson(hero))));

        //        Result result = myHttpServiceCallingObject.doSomething();
        //        assertThat(result.wasSuccessful(), is(true));

        verify(
            postRequestedFor(urlMatching("/"))
//                .withRequestBody(matching(".*<message>1234</message>.*"))
                .withHeader("Content-Type", notMatching(APPLICATION_JSON))
        );*/
    }

    private String toJson(final Applicant hero) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(hero);
    }
}
