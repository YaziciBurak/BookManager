package com.example.bookapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements  AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = "{\"timestamp\":\"" + java.time.LocalDateTime.now() + "\"," +
                "\"status\":401," +
                "\"error\":\"Unauthorized\"," +
                "\"message\":\"Please login first.\"}";

        response.getWriter().write(json);
    }
}
