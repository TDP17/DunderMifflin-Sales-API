package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    fun createEmployee(data: JsonNode): Employee {
        val employee = Employee(
            id = null,
            data.get("name").textValue(),
            data.get("mobile").textValue(),
        )

        return try {
            val createdEmployee = employeeRepository.save(employee)
            employeeRepository.insertIntoHrTable(true, employee.id!!)
            createdEmployee
        } catch (e: Exception) {
            throw e
        }
    }
}