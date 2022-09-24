package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.post
import java.lang.Exception

@SpringBootTest
@AutoConfigureMockMvc
internal class EmployeeServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val employeeRepository: EmployeeRepository, val objectMapper: ObjectMapper
) {
    val baseURL: String = "/employees/"

    @Nested
    @DisplayName("Create Tests")
    inner class PostRouteTests {
        @Test
        fun `should add entry in respective role table as well as base employees table`() {
            // Unsure if the date data type will work correctly here
            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"name\":\"Test Name\",\"mobile\":\"9999999999\",\"role\":\"hr\", \"is_trainer\":true,\"password\":\"password\"}"
            }

            //then
            val result: MvcResult = performPost.andDo { print() }.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }.andReturn()

            val json: String = result.response.contentAsString
            val createdEmployee: Employee = objectMapper.readValue(json, Employee::class.java)
            try {
                val employeeInRoleTable = employeeRepository.fetchFromHr(createdEmployee.id!!)
                assertEquals(false, employeeInRoleTable.isEmpty)
            } catch (e: Exception) {
                println(e)
            }

            // after
            // Implicitly takes care of dropping value from hr table via cascade constraint
            employeeRepository.deleteById(createdEmployee.id!!)
        }
    }
}