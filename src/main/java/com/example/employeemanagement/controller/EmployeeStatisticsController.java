package com.example.employeemanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.projection.DepartmentEmployeeCount;
import com.example.employeemanagement.service.EmployeeStatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class EmployeeStatisticsController {

    private final EmployeeStatisticsService
            employeeStatisticsService;

    public EmployeeStatisticsController(
            EmployeeStatisticsService employeeStatisticsService) {

        this.employeeStatisticsService =
                employeeStatisticsService;
    }

    @GetMapping("/employees/total")
    public ResponseEntity<Map<String, Long>>
            getTotalEmployees() {

        long total =
                employeeStatisticsService
                        .getTotalEmployees();

        Map<String, Long> response =
                new HashMap<>();

        response.put(
                "totalEmployees",
                total
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/by-department")
    public ResponseEntity<List<DepartmentEmployeeCount>>
            getEmployeesByDepartment() {

        return ResponseEntity.ok(
                employeeStatisticsService
                        .getEmployeesByDepartment()
        );
    }
}