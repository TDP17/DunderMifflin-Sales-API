package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Hr
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.Optional

interface HrRepository : CrudRepository<Hr, Int> {
    @Query(
        value = "select * from Hr u where u.employee_id = :employee_id",
        nativeQuery = true
    )
    fun findByEmpId(@Param("employee_id") employee_id: Int): Optional<Hr>
}