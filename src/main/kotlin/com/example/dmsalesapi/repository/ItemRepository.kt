package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long>