package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.service.EmployeeService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class EmployeeController(private val employeeService: EmployeeService) {
    @PostMapping("/employees")
    fun createEmployee(@RequestBody data: JsonNode): ResponseEntity<Employee> {
        val newEmployee: Employee = employeeService.createEmployee(data)
        return ResponseEntity<Employee>(
            newEmployee, HttpStatus.CREATED
        )
    }
}