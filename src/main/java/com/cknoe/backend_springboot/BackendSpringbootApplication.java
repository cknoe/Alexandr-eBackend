package com.cknoe.backend_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BackendSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendSpringbootApplication.class, args);
	}

}
