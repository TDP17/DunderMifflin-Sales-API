package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    fun getEmployees(): MutableIterable<Employee> = employeeRepository.findAll()

    fun getEmployeeById(id: Int): Optional<Employee> = employeeRepository.findById(id)

    fun createEmployee(data: JsonNode): Employee {
        val employee = Employee(
            id = null,
            data.get("name").textValue(),
            data.get("mobile").textValue(),
        )

        return try {
            // @TODO consider a trigger for this instead of 2 operations and their error handling
            val createdEmployee = employeeRepository.save(employee)
            employeeRepository.insertIntoHrTable(true, employee.id!!)
            createdEmployee
        } catch (e: Exception) {
            throw e
        }
    }
}