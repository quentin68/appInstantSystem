package com.quentinbauer.parkingapp.eurekadiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekadiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekadiscoveryApplication.class, args);
	}

}
