package com.example.J_Tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class JTictactoeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JTictactoeApplication.class, args);
	}
}
