package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.EmployeeNotFoundException
import com.example.dmsalesapi.exceptions.FieldNotProvidedException
import com.example.dmsalesapi.exceptions.IdNotFoundException
import com.example.dmsalesapi.model.Customer
import com.example.dmsalesapi.repository.CustomerRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(private val customerRepository: CustomerRepository) {
    fun getCustomers(): MutableIterable<Customer> = customerRepository.findAll()

    fun getCustomerById(id: Int): Optional<Customer> = customerRepository.findById(id)

    fun createCustomer(customer: Customer): Customer {
        return try {
            customerRepository.save(customer)
        } catch (e: NullPointerException) {
            throw FieldNotProvidedException("Field(s) not provided")
        } catch (e: DataIntegrityViolationException) {
            throw EmployeeNotFoundException("Employee with given ID not found, please check employee records")
        }
    }

    fun updateCustomer(id: Int, _name: String, _mobile: String, employee_id: Int): Customer {
        return try {
            val customer: Optional<Customer> = customerRepository.findById(id)
            if (customer.isEmpty) {
                throw IdNotFoundException("No customer with given details found")
            } else {
                val updatedCustomer: Customer = customer.get()
                updatedCustomer.name = _name
                updatedCustomer.mobile = _mobile
                updatedCustomer.employee_id = employee_id

                customerRepository.save(updatedCustomer)
            }
        } catch (e: DataIntegrityViolationException) {
            throw EmployeeNotFoundException("Employee with given ID not found, please check employee records")
        }
    }

    fun deleteCustomer(id: Int) {
        return try {
            customerRepository.deleteById(id)
        } catch (e: Exception) {
            throw IdNotFoundException("No customer with given details found")
        }
    }
}