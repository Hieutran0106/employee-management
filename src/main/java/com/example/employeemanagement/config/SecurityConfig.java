package com.example.employeemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return converter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {

        http
            .csrf(csrf ->
                    csrf.disable()
            )

            .authorizeHttpRequests(auth ->
                    auth

                        // Không cần đăng nhập
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/actuator/health"
                        )
                        .permitAll()

                        // USER và ADMIN đều được xem
                        .requestMatchers(
                                "/employees",
                                "/employees/list",
                                "/employees/search",
                                "/employees/search-by-department",
                                "/employees/search-page",
                                "/employees/statistics",
                                "/api/statistics/**"
                        )
                        .hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        // Các chức năng Employee còn lại chỉ ADMIN được dùng
                        .requestMatchers(
                                "/employees/**"
                        )
                        .hasRole("ADMIN")

                        // Những URL khác phải đăng nhập
                        .anyRequest()
                        .authenticated()
            )

            .httpBasic(
                    Customizer.withDefaults()
            )

            .oauth2ResourceServer(
                    oauth2 ->
                            oauth2.jwt(
                                    jwt ->
                                            jwt.jwtAuthenticationConverter(
                                                    jwtAuthenticationConverter
                                            )
                            )
            );

        return http.build();
    }
}