package com.example.bookapi.service;

import com.example.bookapi.constants.CookieConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenCookieService {

    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie(CookieConstants.REFRESH_TOKEN, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(CookieConstants.REFRESH_PATH);
        cookie.setMaxAge(CookieConstants.REFRESH_MAX_AGE);
        return cookie;
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie(CookieConstants.REFRESH_TOKEN, null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath(CookieConstants.REFRESH_PATH);
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }
}
