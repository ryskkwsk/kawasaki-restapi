package com.example.kawasakirestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KawasakiRestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KawasakiRestapiApplication.class, args);
	}

}
