package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.service.EmployeeService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping
class EmployeeController(private val employeeService: EmployeeService) {
    @GetMapping("/employees")
    fun getEmployees(request: HttpServletRequest): MutableIterable<Employee> {
        return employeeService.getEmployees()
    }

    @GetMapping("/employees/{id}")
    fun getEmployeeById(@PathVariable id: Int): Optional<Employee> = employeeService.getEmployeeById(id)

    @PostMapping("/employees")
    fun createEmployee(@RequestBody data: JsonNode): ResponseEntity<Employee> {
        val newEmployee: Employee = employeeService.createEmployee(data)
        return ResponseEntity<Employee>(
            newEmployee, HttpStatus.CREATED
        )
    }

    @PatchMapping("/employees/{id}")
    @ResponseBody
    fun updateEmployee(@PathVariable id: Int, @RequestBody employee: Employee): ResponseEntity<String> {
        employeeService.updateEmployee(id, employee.name, employee.mobile)
        return ResponseEntity<String>(
            "Updated successfully", HttpStatus.NO_CONTENT
        )
    }

    @DeleteMapping("employees/{id}")
    @ResponseBody
    fun deleteEmployee(@PathVariable id: Int): ResponseEntity<String> {
        employeeService.deleteEmployee(id)
        return ResponseEntity<String>(
            "Deleted successfully", HttpStatus.NO_CONTENT
        )
    }
}