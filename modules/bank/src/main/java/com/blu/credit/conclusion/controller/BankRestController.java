package com.blu.credit.conclusion.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blu.credit.conclusion.dto.ApplicantsData;
import com.blu.credit.conclusion.model.CreditConclusion;
import com.blu.credit.conclusion.reactor.services.BankFlowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BankRestController {

    private final BankFlowService bankFlowService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String healthCheck() {
        return "I'm a Bank, I give credits, I'm OK";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/react")
    public CreditConclusion getCreditConclusion(@RequestBody final ApplicantsData applicants) {
        return bankFlowService.getCreditConclusion(applicants);
    }
}
