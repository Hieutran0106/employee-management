package com.example.employeemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.projection.DepartmentEmployeeCount;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(
            String name
    );

    List<Employee> findByDepartmentNameIgnoreCase(
            String departmentName
    );

    @Query("""
            SELECT
                d.name AS departmentName,
                COUNT(e.id) AS employeeCount
            FROM Employee e
            JOIN e.department d
            GROUP BY d.id, d.name
            ORDER BY d.name
            """)
    List<DepartmentEmployeeCount>
            countEmployeesByDepartment();
}