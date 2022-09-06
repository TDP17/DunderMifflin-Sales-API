package com.example.dmsalesapi.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Hr(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    var is_trainer: Boolean,
    var employee_id: Int,
)