package com.dowadream.errand_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 심부름 서비스 애플리케이션의 메인 클래스
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ErrandServiceApplication {

	/**
	 * 애플리케이션의 진입점
	 * @param args 명령행 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(ErrandServiceApplication.class, args);
	}
}