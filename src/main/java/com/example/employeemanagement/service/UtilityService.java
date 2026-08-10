package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

import com.example.employeemanagement.config.EmployeeCodeFormatter;

@Service
public class UtilityService {

    private final EmployeeCodeFormatter employeeCodeFormatter;

    private int employeeNumber = 1;

    public UtilityService(EmployeeCodeFormatter employeeCodeFormatter) {
        this.employeeCodeFormatter = employeeCodeFormatter;
    }

    public String generateEmployeeCode() {

        String code = employeeCodeFormatter.format(employeeNumber);

        employeeNumber++;

        return code;
    }
}