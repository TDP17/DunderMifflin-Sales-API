package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.service.EmployeeService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping
class EmployeeController(private val employeeService: EmployeeService) {
    @GetMapping("/employees")
    fun getEmployees(): MutableIterable<Employee> = employeeService.getEmployees()

    @GetMapping("/employees/{id}")
    fun getEmployeeById(@PathVariable id: Int): Optional<Employee> = employeeService.getEmployeeById(id)

    @PostMapping("/employees")
    fun createEmployee(@RequestBody data: JsonNode): ResponseEntity<Employee> {
        val newEmployee: Employee = employeeService.createEmployee(data)
        return ResponseEntity<Employee>(
            newEmployee, HttpStatus.CREATED
        )
    }
}