package com.movie_app.users_service.domain.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.List;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //First we search the token in the Authorization header
        String header = request.getHeader("Authorization");
        String token = null;

        if (header != null &&  header.startsWith("Bearer ")) {
            token = header.substring(7);   //Trim the "Bearer" word so that only the code remains
        }

        //Now if we have a token and it's valid, we tell Spring Security who's logged in
        if (token != null && jwtProvider.validateJwtToken(token)) {
            Claims claims = jwtProvider.getClaimsFromToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            //Transform the role into a format that Spring understands
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            //Create a Spring security "passport"
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

            //Put it in context so that the controllers know who made the request
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
