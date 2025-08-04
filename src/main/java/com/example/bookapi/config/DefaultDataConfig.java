package com.example.bookapi.config;

import com.example.bookapi.entity.Role;
import com.example.bookapi.enums.RoleType;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
public class DefaultDataConfig {

    @Bean
    public CommandLineRunner addDefaults(RoleRepository roleRepository,
                                         UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {
                roleRepository.save(Role.builder().name(RoleType.ROLE_USER).build());
            }
        };
    }
}
