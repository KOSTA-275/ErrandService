package com.dowadream.errand_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class ErrandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErrandServiceApplication.class, args);
	}
}