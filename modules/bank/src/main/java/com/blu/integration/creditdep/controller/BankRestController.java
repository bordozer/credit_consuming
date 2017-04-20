package com.blu.integration.creditdep.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankRestController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String healthCheck() {
        return "I'm a Bank, I give credits, I'm OK";
    }
}
