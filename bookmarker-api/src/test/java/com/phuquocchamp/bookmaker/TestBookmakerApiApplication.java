package com.phuquocchamp.bookmaker;

import org.springframework.boot.SpringApplication;

public class TestBookmakerApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(BookmakerApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
