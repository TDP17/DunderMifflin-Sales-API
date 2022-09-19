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
        value = "INSERT INTO manager(branch, employee_id) VALUES (:branch, :employee_id)", nativeQuery = true
    )
    fun insertIntoManagerTable(@Param("branch") branch: String, @Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM employee JOIN manager ON employee.id=manager.employee_id WHERE employee_id=:employee_id",
        nativeQuery = true
    )
    fun fetchFromManager(@Param("employee_id") employee_id: Int): Optional<String>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO hr(is_trainer, employee_id) VALUES (:is_trainer, :employee_id)", nativeQuery = true
    )
    fun insertIntoHrTable(@Param("is_trainer") is_trainer: Boolean, @Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM employee JOIN hr ON employee.id=hr.employee_id WHERE employee_id=:employee_id",
        nativeQuery = true
    )
    fun fetchFromHr(@Param("employee_id") employee_id: Int): Optional<String>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO salesperson(number_of_sales, employee_id) VALUES (:number_of_sales, :employee_id)",
        nativeQuery = true
    )
    fun insertIntoSalespersonTable(@Param("number_of_sales") number_of_sales: Int, @Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM employee JOIN salesperson ON employee.id=salesperson.employee_id WHERE employee_id=:employee_id",
        nativeQuery = true
    )
    fun fetchFromSalesperson(@Param("employee_id") employee_id: Int): Optional<String>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO accountant(employee_id) VALUES (:employee_id)", nativeQuery = true
    )
    fun insertIntoAccountantTable(@Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM employee JOIN accountant ON employee.id=accountant.employee_id WHERE employee_id=:employee_id",
        nativeQuery = true
    )
    fun fetchFromAccountant(@Param("employee_id") employee_id: Int): Optional<String>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO darryl(employee_id) VALUES (:employee_id)", nativeQuery = true
    )
    fun insertIntoDarrylTable(@Param("employee_id") id: Int)

    @Query(
        value = "SELECT name FROM employee JOIN darryl ON employee.id=darryl.employee_id WHERE employee_id=:employee_id",
        nativeQuery = true
    )
    fun fetchFromDarryl(@Param("employee_id") employee_id: Int): Optional<String>

    fun findByName(name: String): Employee
}