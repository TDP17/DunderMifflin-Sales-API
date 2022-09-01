package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Item
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Item, Long> {
    fun findByName(name: String): Item?
}