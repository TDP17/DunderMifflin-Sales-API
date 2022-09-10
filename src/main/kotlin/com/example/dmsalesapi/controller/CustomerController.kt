package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Customer
import com.example.dmsalesapi.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping
@RestController
class CustomerController(private val customerService: CustomerService) {
    @GetMapping("/customers")
    fun getCustomers(): MutableIterable<Customer> = customerService.getCustomers()

    @GetMapping("/customers/{id}")
    fun getCustomerById(@PathVariable id: Int): Optional<Customer> = customerService.getCustomerById(id)

    @PostMapping("/customers")
    fun createCustomer(@RequestBody customer: Customer): ResponseEntity<Customer> {
        val newCustomer: Customer = customerService.createCustomer(customer)
        return ResponseEntity<Customer>(
            newCustomer, HttpStatus.CREATED
        )
    }

    @PatchMapping("/customers/{id}")
    @ResponseBody
    fun updateCustomer(@PathVariable id: Int, @RequestBody customer: Customer): ResponseEntity<String> {
        customerService.updateCustomer(id, customer.name, customer.mobile, customer.employee_id)
        return ResponseEntity<String>(
            "Updated successfully",
            HttpStatus.NO_CONTENT
        )
    }

    @DeleteMapping("customers/{id}")
    @ResponseBody
    fun deleteCustomer(@PathVariable id: Int): ResponseEntity<String> {
        customerService.deleteCustomer(id)
        return ResponseEntity<String>(
            "Deleted successfully",
            HttpStatus.NO_CONTENT
        )
    }
}