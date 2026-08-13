package com.example.employeemanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employeemanagement.dto.RegisterRequest;
import com.example.employeemanagement.exception.UsernameAlreadyExistsException;
import com.example.employeemanagement.model.AppUser;
import com.example.employeemanagement.model.Role;
import com.example.employeemanagement.repository.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository =
                appUserRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    public AppUser register(
            RegisterRequest request) {

        if (appUserRepository
                .existsByUsername(
                        request.getUsername())) {

            throw new UsernameAlreadyExistsException(
                    request.getUsername()
            );
        }

        AppUser user =
                new AppUser();

        user.setUsername(
                request.getUsername()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.USER);

        return appUserRepository.save(user);
    }
}