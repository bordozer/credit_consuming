package com.blu.credit.conclusion.integration.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

@Configuration
public class ExecutorServiceConfiguration {

    @Bean
    public ExecutorService applicantExecutorService() {
        return createExecutorService(2, "applicant-executor-");
    }

    private static ExecutorService createExecutorService(final int nThreads, final String threadNamePrefix) {
        final ThreadFactory threadFactory = new CustomizableThreadFactory(threadNamePrefix);
        return Executors.newFixedThreadPool(nThreads, threadFactory);
    }
}
