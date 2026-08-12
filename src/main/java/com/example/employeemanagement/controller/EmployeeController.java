package com.example.employeemanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {

        logger.debug("REST API: get all employees");

        return employeeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee) {

        logger.info(
                "Creating employee through REST API: name={}, email={}",
                employee.getName(),
                employee.getEmail()
        );

        Employee savedEmployee =
                employeeRepository.save(employee);

        logger.info(
                "Employee created successfully: id={}",
                savedEmployee.getId()
        );

        return ResponseEntity
                .status(201)
                .body(savedEmployee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(
            @PathVariable Long id) {

        logger.debug(
                "REST API: get employee with id={}",
                id
        );

        return employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee updatedEmployee) {

        logger.info(
                "Updating employee with id={}",
                id
        );

        Employee employee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(id));

        employee.setName(
                updatedEmployee.getName()
        );

        employee.setEmail(
                updatedEmployee.getEmail()
        );

        employee.setDepartment(
                updatedEmployee.getDepartment()
        );

        Employee savedEmployee =
                employeeRepository.save(employee);

        logger.info(
                "Employee updated successfully: id={}, name={}",
                savedEmployee.getId(),
                savedEmployee.getName()
        );

        return ResponseEntity.ok(savedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        logger.info(
                "Deleting employee with id={}",
                id
        );

        if (!employeeRepository.existsById(id)) {

            logger.warn(
                    "Cannot delete employee because id={} does not exist",
                    id
            );

            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);

        logger.info(
                "Employee deleted successfully: id={}",
                id
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam String name) {

        logger.debug(
                "REST API: search employee by name={}",
                name
        );

        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/search-by-department")
    public List<Employee> searchByDepartment(
            @RequestParam String department) {

        logger.debug(
                "REST API: search employee by department={}",
                department
        );

        return employeeRepository
                .findByDepartmentNameIgnoreCase(department);
    }
}