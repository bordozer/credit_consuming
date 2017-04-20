package com.blu.integration.creditdep.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

@Configuration
public class ExecutorServiceConfiguration {

    @Bean
    public ThreadPoolExecutorFactoryBean ioBoundExecutorService() {
        final ThreadPoolExecutorFactoryBean result = new ThreadPoolExecutorFactoryBean();
        result.setCorePoolSize(20);
        return result;
    }
}
