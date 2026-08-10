package com.example.employeemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.service.UtilityService;

@RestController
public class HelloController {

    private final UtilityService utilityService;

    public HelloController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Employee Management!";
    }

    @GetMapping("/employee-code")
    public String generateEmployeeCode() {
        return utilityService.generateEmployeeCode();
    }
}