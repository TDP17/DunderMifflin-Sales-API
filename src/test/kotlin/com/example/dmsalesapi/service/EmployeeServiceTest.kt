package com.example.dmsalesapi.service

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
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val employeeRepository: EmployeeRepository,
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
                content = "{\"name\":\"Test Name\",\"mobile\":\"9999999999\",\"role\":\"hr\"}"
            }

            //then
            val result: MvcResult = performPost.andDo { print() }.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }.andReturn()

            // Weird hacks here, REFACTOR
            val id = result.response.contentAsString.substring(6, 8).toInt()

            try {
                val employeeInRoleTable = employeeRepository.fetchFromHr(id)
                assertEquals(false, employeeInRoleTable.isEmpty)
            } catch (e: Exception) {
                println(e)
            }
//
//            // after
//            employeeRepository.deleteById(id);
        }
    }
}