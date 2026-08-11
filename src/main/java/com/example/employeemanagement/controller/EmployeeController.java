package com.example.employeemanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

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

    private final EmployeeRepository employeeRepository;

    public EmployeeController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    // Lấy toàn bộ nhân viên
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Thêm nhân viên mới
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee) {

        Employee savedEmployee =
                employeeRepository.save(employee);

        return ResponseEntity
                .status(201)
                .body(savedEmployee);
    }

    // Lấy nhân viên theo ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(
            @PathVariable Long id) {

        return employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));
    }

    // Cập nhật nhân viên
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee updatedEmployee) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setDepartment(updatedEmployee.getDepartment());

        Employee savedEmployee =
                employeeRepository.save(employee);

        return ResponseEntity.ok(savedEmployee);
    }

    // Xóa nhân viên
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // Tìm nhân viên theo tên
    @GetMapping("/search")
    public List<Employee> searchEmployees(
            @RequestParam String name) {

        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }

    // Tìm nhân viên theo phòng ban
    @GetMapping("/search-by-department")
    public List<Employee> searchByDepartment(
            @RequestParam String department) {

        return employeeRepository
                .findByDepartmentNameIgnoreCase(department);
    }
}