package com.rahim.batchimport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BatchImportApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchImportApplication.class, args);
	}

}
