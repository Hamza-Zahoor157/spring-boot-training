package com.redmath.Lecture02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Lecture02Application {

	public static void main(String[] args) {
		SpringApplication.run(Lecture02Application.class, args);
	}

}
