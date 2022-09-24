package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.EmployeeNotFoundException
import com.example.dmsalesapi.exceptions.UnauthorizedException
import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.example.dmsalesapi.util.BcryptPasswordHashService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val employeeRepository: EmployeeRepository) {
    fun login(email: String, password: String): String {
        try {
            val emp: Employee = employeeRepository.findByEmail(email)

            val matcher = BcryptPasswordHashService()

            if (matcher.matchesPassword(emp.password, password)) {
                val jwt = Jwts.builder().setIssuer(emp.role_code.toString())
                    .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .signWith(SignatureAlgorithm.HS512, "dummysecret").compact()

                return jwt
            } else throw UnauthorizedException("Credentials do not match")
        } catch (e: EmptyResultDataAccessException) {
            throw EmployeeNotFoundException("Employee with given email not found, please check employee records")
        }
    }
}