package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.LoginDTO
import com.example.dmsalesapi.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class AuthController(private val authService: AuthService) {
    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginDTO) = authService.login(request.name)
}