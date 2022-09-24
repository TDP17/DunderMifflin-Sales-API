package com.example.dmsalesapi.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class BcryptPasswordHashService() {
    private val bCryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    fun hash(rawPassword: String): String {
        return bCryptPasswordEncoder.encode(rawPassword)

    }

    fun matchesPassword(hashedPassword: String, rawPassword: String): Boolean {
        return bCryptPasswordEncoder.matches(rawPassword, hashedPassword)
    }
}