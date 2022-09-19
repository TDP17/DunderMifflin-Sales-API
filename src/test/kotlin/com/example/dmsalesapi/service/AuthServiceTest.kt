package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import org.junit.jupiter.api.Assertions
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
internal class AuthServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val employeeRepository: EmployeeRepository
) {
    val baseURL: String = "/auth/login"

    @Nested
    @DisplayName("Login Failed Tests")
    inner class FailedPostRouteTests {
        @Test
        fun `should throw an error if given name is not found`() {
            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"name\":\"Test Name NOT TAKEN EVER\"}"
            }

            //then
            performPost.andDo { print() }.andExpect {
                status { isBadRequest() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json("{\"status\":400,\"message\":\"Employee with given name not found, please check employee records\"}")
                }
            }.andReturn()
        }
    }

    @Nested
    @DisplayName("Login Success Tests")
    inner class SuccessfulPostRouteTests {
        @Test
        fun `should return jwt if given name is found`() {
            val employee = Employee(id = null, name = "TAKEN", mobile = "5235235325", role_code = 2)

            val savedEmployee = employeeRepository.save(employee)

            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"name\":\"TAKEN\"}"
            }

            //then
            val result = performPost.andDo { print() }.andExpect {
                status { isOk() }
            }.andReturn()

            Assertions.assertEquals(true, result.response.contentAsString.isNotEmpty())

            // after
            employeeRepository.deleteById(savedEmployee.id!!)
        }
    }
}