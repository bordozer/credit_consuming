package com.blu.credit.conclusion.integration.config.channel;

import java.util.concurrent.ExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.MessageChannel;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MyChannels {

    private final ExecutorService ioBoundExecutorService;

    @Bean
    public MessageChannel policeChannel() {
        return new ExecutorChannel(ioBoundExecutorService);
    }

    @Bean
    public MessageChannel successCreditConclusionChannel() {
        return new ExecutorChannel(ioBoundExecutorService);
    }

    @Bean
    public MessageChannel refusedCreditConclusionChannel() {
        return new ExecutorChannel(ioBoundExecutorService);
    }
}
