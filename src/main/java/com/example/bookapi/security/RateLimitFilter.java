package com.example.bookapi.security;

import com.example.bookapi.constants.ErrorMessages;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if(isRateLimitedEndPoint(requestURI)) {
            String ip = request.getRemoteAddr();
            Bucket bucket = buckets.computeIfAbsent(ip, this::createNewBucket);


        if(bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        }
        else {
            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(429);
            response.getWriter().write(ErrorMessages.TOO_MANY_REQUEST);
            response.getWriter().flush();
        }
        }
        else {
            filterChain.doFilter(request, response);
        }

    }

    private Bucket createNewBucket(String key) {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    private boolean isRateLimitedEndPoint(String uri) {
        return uri.equals("/api/auth/login") || uri.equals("/api/auth/register");
    }
}
