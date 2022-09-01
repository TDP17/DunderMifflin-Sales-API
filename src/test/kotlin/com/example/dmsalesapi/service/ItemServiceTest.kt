package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Item
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/pretest.sql")
internal class ItemServiceTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseURL: String = "/items/";

    @Test
    fun `should add the new item if name is not duplicate`() {
        // given

        val newItem = Item(id=100, name = "New paper", price = 2000, quantity_available = 10)

        // when
        val performPost = mockMvc.post(baseURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newItem)
        }

        // then
        performPost
            .andDo { print() }
            .andExpect {
                status { isCreated() }
            }
    }
}