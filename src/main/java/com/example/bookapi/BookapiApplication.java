package com.example.bookapi;

import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class BookapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookapiApplication.class, args);
	}

	@Bean
	public CommandLineRunner addDefaults(RoleRepository roleRepository,
										 UserRepository userRepository,
										 PasswordEncoder passwordEncoder) {
		return args -> { if (roleRepository.findByName("ROLE_USER").isEmpty()) {
			roleRepository.save(new Role(null, "ROLE_USER"));
		}
		if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
			roleRepository.save(new Role(null, "ROLE_ADMIN"));
		}
		if(userRepository.findByUsername("admin").isEmpty()) {
			Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

			User admin = User.builder()
					.username("admin")
					.password(passwordEncoder.encode("123"))
					.roles(Set.of(adminRole))
					.build();

			userRepository.save(admin);
		}
		};
	}
}
