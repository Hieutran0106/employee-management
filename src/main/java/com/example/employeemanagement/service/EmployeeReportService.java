package com.example.employeemanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.employeemanagement.repository.EmployeeRepository;

@Service
public class EmployeeReportService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    EmployeeReportService.class
            );

    private final EmployeeRepository employeeRepository;

    public EmployeeReportService(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Cacheable("employeeCount")
    public long getTotalEmployees() {

        logger.info(
                "Querying employee count from database"
        );

        return employeeRepository.count();
    }
}