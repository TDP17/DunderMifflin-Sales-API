package com.example.dmsalesapi.model

import java.sql.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Sale(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int?,
    val employee_id: Int,
    val customer_id: Int,
    val date: Date?
)

class SalePostEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int?,
    val employee_id: Int,
    val customer_id: Int,
    val itemList: List<ItemQuantityMappingEntity>
)
