package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.EmployeeRepository
import com.example.dmsalesapi.repository.ItemRepository
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
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class EmployeeServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val objectMapper: ObjectMapper, val employeeRepository: EmployeeRepository
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
                content =
                    "{\"id\":1,\"name\":\"Test Name\",\"mobile\":\"9999999999\",\"joining_date\":\"2019-01-22\",\"role\":\"hr\"}"
            }

            //then
            performPost.andDo { print() }.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }

            employeeRepository.findEmployeeByRoleAndId("hr", 1)

            // after
            // employeeRepository.deleteById(employee.id!!);
        }
    }
}