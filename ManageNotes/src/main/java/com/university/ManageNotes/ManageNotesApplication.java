package com.university.ManageNotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ManageNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageNotesApplication.class, args);
	}

}
