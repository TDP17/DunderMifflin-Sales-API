package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.LoginDTO
import com.example.dmsalesapi.service.AuthService
import com.example.dmsalesapi.util.APIResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class AuthController(private val authService: AuthService) {
    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginDTO): ResponseEntity<APIResponse> {
        val token = authService.login(request.email, request.password)

        val res = APIResponse(HttpStatus.OK.value(), token)
        return ResponseEntity<APIResponse>(res, HttpStatus.OK)
    }
}