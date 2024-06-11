package com.sogeti.java.anonymous_artist.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(403);
        response.getWriter().write("Oh oh! It seems that you don't have the rights to perform this action. Please contact an administrator." + "\ntimestamp: " + LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }
}
