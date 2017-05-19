package com.blu.credit.conclusion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ThreadPoolExecutorFactoryBean ioBoundExecutorService() {
		final ThreadPoolExecutorFactoryBean result = new ThreadPoolExecutorFactoryBean();
		result.setCorePoolSize(20);
		return result;
	}
}
