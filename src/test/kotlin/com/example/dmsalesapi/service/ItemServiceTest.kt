package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class ItemServiceTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val itemRepository: ItemRepository
) {
    val baseURL: String = "/items/";

    @Test
    fun `should add the new item if name is not duplicate`() {
        //before
        val price: Int = 2000
        val quantity_available: Int = 2000
        itemRepository.removeByName("NAMEWHICHWILLNEVERBECREATED")

        // given
        val newItem = Item(id = 100, name = "NAMEWHICHWILLNEVERBECREATED", price = price, quantity_available = quantity_available)

        // when
        val performPost = mockMvc.post(baseURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newItem)
        }


        // then
        performPost
//            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
            .andDo {
                print(content())
            }

        // after
        itemRepository.removeByName("NAMEWHICHWILLNEVERBECREATED");
    }
}