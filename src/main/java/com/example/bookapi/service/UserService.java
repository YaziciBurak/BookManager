package com.example.bookapi.service;

import com.example.bookapi.constants.ErrorMessages;
import com.example.bookapi.entity.Role;
import com.example.bookapi.entity.User;
import com.example.bookapi.enums.RoleType;
import com.example.bookapi.repository.RoleRepository;
import com.example.bookapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void addRoleToUser(String username, RoleType roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ROLE_NOT_FOUND));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}
