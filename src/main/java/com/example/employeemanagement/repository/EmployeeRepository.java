package com.example.employeemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeemanagement.model.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    // Tìm nhân viên theo tên
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Tìm nhân viên theo phòng ban
    List<Employee> findByDepartmentNameIgnoreCase(String departmentName);
}