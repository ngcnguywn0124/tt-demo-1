package com.example.demo_blog_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/home/demo").permitAll()
                        .requestMatchers("/admin/**", "/blogs/admin", "/blogs/*/comments/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/blogs/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/blogs", "/blogs/*", "/blogs/slug/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/blogs/*/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories", "/categories/*", "/categories/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tags", "/tags/*", "/tags/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categories", "/tags").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**", "/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**", "/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/blogs").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/blogs/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/blogs/**", "/comments/*/status").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/blogs/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/blogs/*/comments").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
