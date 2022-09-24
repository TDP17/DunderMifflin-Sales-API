package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.DuplicateFieldException
import com.example.dmsalesapi.exceptions.FieldNotProvidedException
import com.example.dmsalesapi.exceptions.IdNotFoundException
import com.example.dmsalesapi.exceptions.IncorrectFieldValueException
import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.example.dmsalesapi.util.BcryptPasswordHashService
import com.fasterxml.jackson.databind.JsonNode
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.Optional

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    fun getEmployees(): MutableIterable<Employee> = employeeRepository.findAll()

    fun getEmployeeById(id: Int): Optional<Employee> = employeeRepository.findById(id)

    fun createEmployee(data: JsonNode): Employee {
        val role: String
        try {
            role = data.get("role").textValue()
        } catch (e: Exception) {
            throw FieldNotProvidedException("Field `role` not provided")
        }

        val roleCode: Int = when (role) {
            "manager" -> 1
            "salesperson" -> 2
            "hr" -> 3
            "accountant" -> 4
            "darryl" -> 5
            else -> throw IncorrectFieldValueException("Field `role` provided has an incorrect value")
        }

        val rawPassword: String = data.get("password").textValue()
        val hasher = BcryptPasswordHashService()
        val password: String = hasher.hash(rawPassword)

        val employee = Employee(
            id = null,
            data.get("name").textValue(),
            data.get("mobile").textValue(),
            role_code = roleCode,
            password,
            email = data.get("email").textValue()
        )

        fun handleManager() {
            try {
                val branch = data.get("branch").textValue()
                employeeRepository.insertIntoManagerTable(branch, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `branch` not provided")
            }
        }

        fun handleHr() {
            try {
                val isTrainer = data.get("is_trainer").booleanValue()
                employeeRepository.insertIntoHrTable(isTrainer, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `is_trainer` not provided")
            }
        }

        fun handleSalesperson() {
            try {
                val noOfSales = data.get("no_of_sales").intValue()
                employeeRepository.insertIntoSalespersonTable(noOfSales, employee.id!!)
            } catch (e: NullPointerException) {
                throw FieldNotProvidedException("Field `no_of_sales` not provided")
            }
        }

        return try {
            // @TODO consider a trigger for this instead of 2 operations and their error handling
            val createdEmployee = employeeRepository.save(employee)
            when (role) {
                "manager" -> handleManager()
                "salesperson" -> handleSalesperson()
                "hr" -> handleHr()
                "accountant" -> employeeRepository.insertIntoAccountantTable(employee.id!!)
                "darryl" -> employeeRepository.insertIntoDarrylTable(employee.id!!)
            }
            createdEmployee.password = ""
            createdEmployee
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateFieldException("Field `email` provided already exists")
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