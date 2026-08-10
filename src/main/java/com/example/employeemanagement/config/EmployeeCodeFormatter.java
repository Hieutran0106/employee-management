package com.example.employeemanagement.config;

public class EmployeeCodeFormatter {

    public String format(int number) {
        return String.format("EMP-%04d", number);
    }
}