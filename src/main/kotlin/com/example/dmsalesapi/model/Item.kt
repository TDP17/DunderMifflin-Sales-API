package com.example.dmsalesapi.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Item (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val name: String,
    val quantity_available: Int,
    val price: Int,
)