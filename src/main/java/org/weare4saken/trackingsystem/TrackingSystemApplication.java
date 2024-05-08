package org.weare4saken.trackingsystem;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.weare4saken.trackingsystem.generator.impl.FilmGenerator;

@Slf4j
@EnableAsync
@AllArgsConstructor
@SpringBootApplication
public class TrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingSystemApplication.class, args);
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner commandLineRunner(FilmGenerator filmGenerator) {
		return args -> {
			Long filmsAmount = 100000L;
			filmGenerator.generate(filmsAmount);
			filmGenerator.generateAsFuture(filmsAmount);
			filmGenerator.generateAsCompletableFuture(filmsAmount);
		};
	}
}
