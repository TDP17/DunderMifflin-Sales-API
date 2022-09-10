package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.FieldNotProvidedException
import com.example.dmsalesapi.exceptions.IdNotFoundException
import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import java.lang.NullPointerException
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

        fun handleManager() {
            try {
                val branch = data.get("branch").textValue()
                employeeRepository.insertIntoManagerTable(branch, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `branch` not provided");
            }
        }

        fun handleHr() {
            try {
                val isTrainer = data.get("is_trainer").booleanValue()
                employeeRepository.insertIntoHrTable(isTrainer, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `is_trainer` not provided");
            }
        }

        fun handleSalesperson() {
            try {
                val noOfSales = data.get("no_of_sales").intValue()
                employeeRepository.insertIntoSalespersonTable(noOfSales, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `no_of_sales` not provided");
            }
        }

        return try {
            val role: String = data.get("role").textValue()
            // @TODO consider a trigger for this instead of 2 operations and their error handling
            val createdEmployee = employeeRepository.save(employee)
            when (role) {
                "manager" -> handleManager()
                "salesperson" -> handleSalesperson()
                "hr" -> handleHr()
                "accountant" -> employeeRepository.insertIntoAccountantTable(employee.id!!)
                "darryl" -> employeeRepository.insertIntoDarrylTable(employee.id!!)
            }

            createdEmployee
        } catch (e: NullPointerException) {
            throw FieldNotProvidedException("Field `role` not provided");
        }
    }

    fun updateEmployee(id: Int, _name: String, _mobile: String): Employee {
        return try {
            val employee: Optional<Employee> = employeeRepository.findById(id)
            if (employee.isEmpty) {
                throw IdNotFoundException("No employee with given details found")
            } else {
                val updatedEmployee: Employee = employee.get()
                updatedEmployee.name = _name
                updatedEmployee.mobile = _mobile

                employeeRepository.save(updatedEmployee)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteEmployee(id: Int) {
        return try {
            employeeRepository.deleteById(id)
        } catch (e: Exception) {
            throw IdNotFoundException("No employee with given details found")
        }
    }
}