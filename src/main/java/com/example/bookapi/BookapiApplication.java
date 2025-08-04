package com.example.bookapi;

import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.enums.RoleType;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class BookapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookapiApplication.class, args);
	}

}
