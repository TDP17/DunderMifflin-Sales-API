package com.example.dmsalesapi.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class EmployeeController {
    @PostMapping("/employees")
    fun createEmployee() {

    }
}