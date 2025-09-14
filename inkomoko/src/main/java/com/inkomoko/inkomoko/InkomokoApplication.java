package com.inkomoko.inkomoko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class InkomokoApplication {

	public static void main(String[] args) {
		SpringApplication.run(InkomokoApplication.class, args);
	}

}
