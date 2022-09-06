package com.example.dmsalesapi.model

import java.sql.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Employee (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    var name: String,
    var mobile: String,
)