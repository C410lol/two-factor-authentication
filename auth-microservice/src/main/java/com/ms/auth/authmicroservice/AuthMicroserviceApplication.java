package com.ms.auth.authmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class AuthMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthMicroserviceApplication.class, args);
	}

}
