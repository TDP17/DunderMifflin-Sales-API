package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.EmployeeNotFoundException
import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val employeeRepository: EmployeeRepository) {
    fun login(name: String): String {
        try {
            val emp: Employee = employeeRepository.findByName(name)

            val jwt =
                Jwts.builder().setIssuer(emp.role_code.toString()).setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS512, "dummysecret").compact()

            return jwt
        } catch (e: EmptyResultDataAccessException) {
            throw EmployeeNotFoundException("Employee with given name not found, please check employee records")
        }
    }
}