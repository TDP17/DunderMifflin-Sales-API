package com.example.dmsalesapi.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Employee(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int?,
    var name: String,
    var mobile: String,
    val role_code: Int,
    var password: String,
    val email: String,
)

class LoginDTO(
    val email: String,
    val password: String,
)