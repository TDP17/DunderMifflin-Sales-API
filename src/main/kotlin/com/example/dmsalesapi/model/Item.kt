package com.example.dmsalesapi.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Item(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
    var name: String,
    var quantity_available: Int,
    var price: Int
)

class ItemQuantityMappingEntity(
    val id: Int,
    val quantity: Int
)