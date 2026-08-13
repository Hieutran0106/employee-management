package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(
            min = 6,
            message = "Password must be at least 6 characters"
    )
    private String password;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username) {

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password) {

        this.password = password;
    }
}