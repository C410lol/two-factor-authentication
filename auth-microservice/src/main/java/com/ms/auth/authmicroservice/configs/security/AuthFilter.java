package com.ms.auth.authmicroservice.configs.security;

import com.ms.auth.authmicroservice.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override protected void doFilterInternal(@NotNull HttpServletRequest request,
                                              @NotNull HttpServletResponse response,
                                              @NotNull FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null &&
                !authorizationHeader.isBlank() &&
                authorizationHeader.startsWith("Bearer ")) {
            SecurityContextHolder.getContext().setAuthentication(
                    jwtService.tryToAuthenticate(authorizationHeader.substring(7)));
        }
        filterChain.doFilter(request, response);
    }

}