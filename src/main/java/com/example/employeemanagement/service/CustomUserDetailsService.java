package com.example.employeemanagement.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.employeemanagement.model.AppUser;
import com.example.employeemanagement.repository.AppUserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(
            AppUserRepository appUserRepository) {

        this.appUserRepository =
                appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        AppUser appUser =
                appUserRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"
                                )
                        );

        return org.springframework.security.core.userdetails.User
                .withUsername(
                        appUser.getUsername()
                )
                .password(
                        appUser.getPassword()
                )
                .roles(
                        appUser.getRole().name()
                )
                .build();
    }
}