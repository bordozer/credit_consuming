package com.blu.integration.police.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blu.integration.model.Applicant;
import com.blu.integration.model.PoliceResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PoliceRestController {

    private static final List<String> WANTED = Arrays.asList("Julico Banditto", "Ben Laden");

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String healthCheck() {
        return "I'm a Police Department, I'm OK";
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public PoliceResponse getPoliceResponse(@RequestBody final Applicant applicant) {
        log.info("----- Client validation request: {}", applicant);
        if (WANTED.contains(applicant.getName())) {
            return PoliceResponse.criminal(applicant);
        }
        return PoliceResponse.respectable(applicant);
    }
}
