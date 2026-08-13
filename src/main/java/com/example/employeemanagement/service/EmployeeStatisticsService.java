package com.example.employeemanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employeemanagement.projection.DepartmentEmployeeCount;
import com.example.employeemanagement.repository.EmployeeRepository;

@Service
public class EmployeeStatisticsService {

    private final EmployeeRepository employeeRepository;

    public EmployeeStatisticsService(
            EmployeeRepository employeeRepository) {

        this.employeeRepository =
                employeeRepository;
    }

    public long getTotalEmployees() {

        return employeeRepository.count();
    }

    public List<DepartmentEmployeeCount>
            getEmployeesByDepartment() {

        return employeeRepository
                .countEmployeesByDepartment();
    }
}