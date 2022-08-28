package com.example.dmsalesapi.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Item (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String,
    val available: Int,
    val price: Int,
)