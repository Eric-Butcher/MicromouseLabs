package com.micromouselab.mazes.security;

import com.micromouselab.mazes.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthFilter extends OncePerRequestFilter {



    private final AuthService authService;

    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(AuthService authService, UserDetailsService userDetailsService){
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Optional<String> jwt = extractJwtFromHeader(request);

            if (jwt.isPresent()) {
                UserDetails userDetails = authService.validateToken(jwt.get());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        } catch (Exception ignored) {

        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractJwtFromHeader(HttpServletRequest request){
        String authorizationHeaderContents = request.getHeader("Authorization");

        if (authorizationHeaderContents == null){
            return Optional.empty();
        }

        final String bearerPrefix = "Bearer ";
        if (authorizationHeaderContents.startsWith(bearerPrefix)){
            String jwtTokenContents = authorizationHeaderContents.substring(bearerPrefix.length());
            return Optional.of(jwtTokenContents);
        }
        return Optional.empty();
    }
}
