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
		return args -> {
			if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
			roleRepository.save(Role.builder().name(RoleType.ROLE_USER).build());
		}
		if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
			roleRepository.save(Role.builder().name(RoleType.ROLE_ADMIN).build());
		}
		if(userRepository.findByUsername("admin").isEmpty()) {
			Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN).orElseThrow();

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
