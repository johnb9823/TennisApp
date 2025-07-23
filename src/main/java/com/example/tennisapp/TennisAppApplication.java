package com.example.tennisapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TennisAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisAppApplication.class, args);
	}

}
