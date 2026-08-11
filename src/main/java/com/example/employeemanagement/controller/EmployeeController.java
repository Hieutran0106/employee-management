package com.example.employeemanagement.controller;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        Employee savedEmployee =
                employeeRepository.save(employee);

        return ResponseEntity
                .status(201)
                .body(savedEmployee);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        return employeeRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee updatedEmployee) {

        return employeeRepository.findById(id)
                .map(employee -> {

                    employee.setName(
                            updatedEmployee.getName());

                    employee.setEmail(
                            updatedEmployee.getEmail());

                    employee.setDepartment(
                            updatedEmployee.getDepartment());

                    Employee saved =
                            employeeRepository.save(employee);

                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        employeeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam String name) {

        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }
    @GetMapping("/search-by-department")
    public List<Employee> searchByDepartment(
            @RequestParam String department) {

        return employeeRepository
                .findByDepartmentNameIgnoreCase(department);
    }
    
}