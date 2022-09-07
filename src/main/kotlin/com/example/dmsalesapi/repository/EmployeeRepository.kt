package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Employee
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

interface EmployeeRepository : CrudRepository<Employee, Int> {
    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO hr(is_trainer, employee_id) VALUES (:is_trainer, :employee_id)", nativeQuery = true
    )
    fun insertIntoHrTable(@Param("is_trainer") is_trainer: Boolean, @Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM EMPLOYEE JOIN HR ON EMPLOYEE.ID=HR.EMPLOYEE_ID WHERE EMPLOYEE_ID=:employee_id",
        nativeQuery = true
    )
    fun fetchFromHr(@Param("employee_id") employee_id: Int): Optional<String>
}