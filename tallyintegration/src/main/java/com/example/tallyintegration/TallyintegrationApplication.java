package com.example.tallyintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TallyintegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TallyintegrationApplication.class, args);
	}

}
