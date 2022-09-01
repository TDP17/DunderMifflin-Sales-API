package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Item
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface ItemRepository : CrudRepository<Item, Long> {
    fun findByName(name: String): Item?

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM item u WHERE u.name = :name",
        nativeQuery = true
    )
    fun removeByName(@Param("name") name: String)
}