package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class ItemServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val objectMapper: ObjectMapper, val itemRepository: ItemRepository
) {
    val baseURL: String = "/items/"

    @Test
    fun `should throw an error if item name is duplicate`() {
        //before

        // For some reason if id is any duplicate value, no exception will be thrown but no data will be added either
        // This leads to the below workaround
        // @TODO FIX LATER
        val item = Item(id = null, name = "TestName", quantity_available = 10, price = 10)

        //when
        itemRepository.deleteByName(item.name)

        val tempItem = itemRepository.save(item)
        item.id = tempItem.id?.plus(1)
        val performPost = mockMvc.post(baseURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
        }

        //then
        performPost.andDo { print() }.andExpect {
            status { isBadRequest() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json("{\"status\":400,\"message\":\"Another paper with the same name exists, please update the entity instead\"}")
            }
        }

        // after
        itemRepository.deleteByName(item.name)
    }


    @Test
    fun `should add the new item if name is not duplicate`() {
        //before
        val price: Int = 2000
        val quantity_available: Int = 2000
        itemRepository.deleteByName("NAMEWHICHWILLNEVERBECREATED")

        // given
        val newItem = Item(id = null, name = "NAMEWHICHWILLNEVERBECREATED", price = price, quantity_available = quantity_available)

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

        // after
        itemRepository.deleteByName("NAMEWHICHWILLNEVERBECREATED");
    }
}