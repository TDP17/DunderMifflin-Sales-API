package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository: CrudRepository<Customer, Int>