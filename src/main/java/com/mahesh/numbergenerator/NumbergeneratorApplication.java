package com.mahesh.numbergenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NumbergeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumbergeneratorApplication.class, args);
	}

}
