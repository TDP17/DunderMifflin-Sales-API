package com.example.dmsalesapi.model

import java.io.Serializable
import java.sql.Date
import javax.persistence.*


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

@Entity
@IdClass(CompositeKey::class)
data class ExtendedSale(
    @Id val sale_id: Int,
    val sale_date: Date,
    @Id val item_name: String,
    val quantity: Int,
    val price: Int,
    val employee_name: String,
    val customer_name: String
)

class CompositeKey : Serializable {
    private val sale_id: Int = 0
    private val item_name: String = ""
}