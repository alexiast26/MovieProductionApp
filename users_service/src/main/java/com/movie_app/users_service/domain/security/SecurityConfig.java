package com.movie_app.users_service.domain.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())          //Deactivate CSRF since we use JWT tokens, not browser sessions
                //Configure access rules
                .authorizeHttpRequests(auth-> auth.requestMatchers(
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",               //Allow free access to Swagger routes for documentation
                        "/swagger-ui.html"
                ).permitAll()
                //Permit free access for Register and Login access
                .requestMatchers(
                        "/api/users/register",
                        "/api/users/login"
                ).permitAll()
                //Permit free acces to /all so that the Report Service can easily access the data
                .requestMatchers("/api/users/all").permitAll()
                .requestMatchers("/api/users/delete/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/users/update/**", "/api/users/role/**").authenticated()
                .anyRequest().authenticated()     //Any other request need the user to be logged in
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
