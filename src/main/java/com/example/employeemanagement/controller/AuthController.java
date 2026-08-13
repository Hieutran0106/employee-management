package com.example.employeemanagement.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.dto.LoginRequest;
import com.example.employeemanagement.dto.LoginResponse;
import com.example.employeemanagement.dto.RegisterRequest;
import com.example.employeemanagement.model.AppUser;
import com.example.employeemanagement.service.AuthService;
import com.example.employeemanagement.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(
            @Valid @RequestBody RegisterRequest request) {

        AppUser user =
                authService.register(request);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        String token =
                jwtService.generateToken(
                        authentication
                );

        String role =
                authentication
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority();

        LoginResponse response =
                new LoginResponse(
                        token,
                        "Bearer",
                        authentication.getName(),
                        role
                );

        return ResponseEntity.ok(response);
    }
}