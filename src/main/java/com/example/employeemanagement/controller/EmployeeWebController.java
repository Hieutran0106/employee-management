package com.example.employeemanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.employeemanagement.service.EmployeeStatisticsService;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeWebController.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeStatisticsService employeeStatisticsService;

    public EmployeeWebController(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            EmployeeStatisticsService employeeStatisticsService) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeStatisticsService = employeeStatisticsService;
    }

    @GetMapping("/list")
    public String showEmployeeList(Model model) {

        logger.debug("Loading employee list");

        List<Employee> employees =
                employeeRepository.findAll();

        logger.info(
                "Loaded {} employees",
                employees.size()
        );

        model.addAttribute(
                "employees",
                employees
        );

        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        logger.debug("Opening add employee form");

        Employee employee = new Employee();
        employee.setDepartment(new Department());

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

        logger.info(
                "Request to add employee: name={}, email={}",
                employee.getName(),
                employee.getEmail()
        );

        if (bindingResult.hasErrors()) {

            logger.warn(
                    "Validation failed when adding employee: name={}, email={}",
                    employee.getName(),
                    employee.getEmail()
            );

            model.addAttribute(
                    "departments",
                    departmentRepository.findAll()
            );

            return "employees/add";
        }

        if (employee.getDepartment() == null
                || employee.getDepartment().getId() == null) {

            logger.warn(
                    "Cannot add employee because department was not selected"
            );

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

            logger.warn(
                    "Department not found with id={}",
                    departmentId
            );

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

        employee.setDepartment(department);

        Employee savedEmployee =
                employeeRepository.save(employee);

        logger.info(
                "Employee added successfully: id={}, name={}",
                savedEmployee.getId(),
                savedEmployee.getName()
        );

        return "redirect:/employees/list";
    }

    @GetMapping("/search-page")
    public String searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Model model) {

        logger.debug(
                "Searching employees: name={}, department={}",
                name,
                department
        );

        List<Employee> employees;

        if (name != null
                && !name.isBlank()) {

            employees =
                    employeeRepository
                            .findByNameContainingIgnoreCase(name);

            logger.info(
                    "Search by name='{}' returned {} employees",
                    name,
                    employees.size()
            );

        } else if (department != null
                && !department.isBlank()) {

            employees =
                    employeeRepository
                            .findByDepartmentNameIgnoreCase(
                                    department
                            );

            logger.info(
                    "Search by department='{}' returned {} employees",
                    department,
                    employees.size()
            );

        } else {

            employees =
                    employeeRepository.findAll();

            logger.debug(
                    "No search condition provided, returning all employees"
            );
        }

        model.addAttribute(
                "employees",
                employees
        );

        return "employees/search";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {

        logger.debug("Loading employee statistics page");

        long totalEmployees =
                employeeStatisticsService
                        .getTotalEmployees();

        model.addAttribute(
                "totalEmployees",
                totalEmployees
        );

        model.addAttribute(
                "departmentStatistics",
                employeeStatisticsService
                        .getEmployeesByDepartment()
        );

        logger.info(
                "Loaded employee statistics: totalEmployees={}",
                totalEmployees
        );

        return "employees/statistics";
    }
}