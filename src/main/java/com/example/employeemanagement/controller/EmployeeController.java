package com.example.employeemanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.model.Employee;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final List<Employee> employees = new ArrayList<>();

    private Long nextId = 3L;

    public EmployeeController() {
        employees.add(new Employee(1L, "Nguyen Van A", "IT"));
        employees.add(new Employee(2L, "Tran Thi B", "HR"));
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        employee.setId(nextId);
        nextId++;

        employees.add(employee);

        return ResponseEntity.status(201).body(employee);
    }
}