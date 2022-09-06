package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Employee
import org.springframework.data.repository.CrudRepository

interface EmployeeRepository : CrudRepository<Employee, Long> {}