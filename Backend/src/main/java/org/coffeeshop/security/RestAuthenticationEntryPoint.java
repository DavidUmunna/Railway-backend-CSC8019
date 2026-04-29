package org.coffeeshop.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"" + resolveMessage(authException) + "\"}");
    }

    private String resolveMessage(AuthenticationException authException) {
        if (authException == null || authException.getMessage() == null || authException.getMessage().isBlank()) {
            return "Authentication required";
        }
        if (authException instanceof BadCredentialsException
                && "Bad credentials".equalsIgnoreCase(authException.getMessage())) {
            return "Invalid username or password";
        }
        return authException.getMessage();
    }
}
