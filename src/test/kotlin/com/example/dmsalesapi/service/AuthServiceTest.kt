package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Employee
import com.example.dmsalesapi.repository.EmployeeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
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

@SpringBootTest
@AutoConfigureMockMvc
internal class AuthServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val employeeRepository: EmployeeRepository, val objectMapper: ObjectMapper
) {
    val baseURL: String = "/auth/login"

    @Nested
    @DisplayName("Login Failed Tests")
    inner class FailedPostRouteTests {
        @Test
        fun `should return an error if given email is not found`() {
            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"email\":\"nevertaken11@gmail.com\", \"password\":\"password\"}"
            }

            //then
            performPost.andDo { print() }.andExpect {
                status { isBadRequest() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json("{\"status\":400,\"message\":\"Employee with given email not found, please check employee records\"}")
                }
            }.andReturn()
        }

        @Test
        fun `should return an error if auth is incorrect`() {
            // before save employee
            val creationResult: MvcResult = mockMvc.post("/employees") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    "{\n" + "    \"name\":\"TestName\",\n" + "    \"email\": \"test@gmail.com\",\n" + "    \"role\": \"darryl\",\n" + "    \"mobile\": \"85729572692\",\n" + "    \"password\": \"password\"\n" + "}"
            }.andReturn()

            val json: String = creationResult.response.contentAsString
            val createdEmployee: Employee = objectMapper.readValue(json, Employee::class.java)

            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"email\":\"test@gmail.com\", \"password\":\"incorrectpassword\"}"
            }

            //then
            val assertionAuthResult = performPost.andDo { print() }.andExpect {
                status { isUnauthorized() }
                content {
                    json("{\"status\":401,\"message\":\"Credentials do not match\"}")
                }
            }.andReturn()

            Assertions.assertEquals(true, assertionAuthResult.response.contentAsString.isNotEmpty())

            // after
            employeeRepository.deleteById(createdEmployee.id!!)
        }
    }

    @Nested
    @DisplayName("Login Success Tests")
    inner class SuccessfulPostRouteTests {
        @Test
        fun `should return jwt if given auth is correct`() {
            // before save employee
            val creationResult: MvcResult = mockMvc.post("/employees") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    "{\n" + "    \"name\":\"TestName\",\n" + "    \"email\": \"test@gmail.com\",\n" + "    \"role\": \"darryl\",\n" + "    \"mobile\": \"85729572692\",\n" + "    \"password\": \"password\"\n" + "}"
            }.andReturn()

            val json: String = creationResult.response.contentAsString
            val createdEmployee: Employee = objectMapper.readValue(json, Employee::class.java)

            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content = "{\"email\":\"test@gmail.com\", \"password\":\"password\"}"
            }

            //then
            val assertionAuthResult = performPost.andDo { print() }.andExpect {
                status { isOk() }
            }.andReturn()

            Assertions.assertEquals(true, assertionAuthResult.response.contentAsString.isNotEmpty())

            // after
            employeeRepository.deleteById(createdEmployee.id!!)
        }
    }
}