package com.example.employeemanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeWebController(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/list")
    public String showEmployeeList(Model model) {

        List<Employee> employees =
                employeeRepository.findAll();

        model.addAttribute(
                "employees",
                employees
        );

        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        Employee employee = new Employee();

        employee.setDepartment(
                new Department()
        );

        model.addAttribute(
                "employee",
                employee
        );

        model.addAttribute(
                "departments",
                departmentRepository.findAll()
        );

        return "employees/add";
    }

    @PostMapping("/add")
    public String addEmployee(
            @Valid @ModelAttribute Employee employee,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "departments",
                    departmentRepository.findAll()
            );

            return "employees/add";
        }

        if (employee.getDepartment() == null
                || employee.getDepartment().getId() == null) {

            model.addAttribute(
                    "departments",
                    departmentRepository.findAll()
            );

            model.addAttribute(
                    "departmentError",
                    "Please select a department"
            );

            return "employees/add";
        }

        Long departmentId =
                employee.getDepartment().getId();

        Department department =
                departmentRepository
                        .findById(departmentId)
                        .orElse(null);

        if (department == null) {

            model.addAttribute(
                    "departments",
                    departmentRepository.findAll()
            );

            model.addAttribute(
                    "departmentError",
                    "Department not found"
            );

            return "employees/add";
        }

        employee.setDepartment(
                department
        );

        employeeRepository.save(
                employee
        );

        return "redirect:/employees/list";
    }

    @GetMapping("/search-page")
    public String searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Model model) {

        List<Employee> employees;

        if (name != null
                && !name.isBlank()) {

            employees =
                    employeeRepository
                            .findByNameContainingIgnoreCase(
                                    name
                            );

        } else if (department != null
                && !department.isBlank()) {

            employees =
                    employeeRepository
                            .findByDepartmentNameIgnoreCase(
                                    department
                            );

        } else {

            employees =
                    employeeRepository.findAll();
        }

        model.addAttribute(
                "employees",
                employees
        );

        return "employees/search";
    }
}