package com.example.employeemanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.service.EmployeeReportService;

@RestController
@RequestMapping("/reports")
public class EmployeeReportController {

    private final EmployeeReportService employeeReportService;

    public EmployeeReportController(
            EmployeeReportService employeeReportService) {

        this.employeeReportService =
                employeeReportService;
    }

    @GetMapping("/employees/count")
    public Map<String, Object> getEmployeeCount() {

        long totalEmployees =
                employeeReportService.getTotalEmployees();

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "totalEmployees",
                totalEmployees
        );

        return response;
    }
}