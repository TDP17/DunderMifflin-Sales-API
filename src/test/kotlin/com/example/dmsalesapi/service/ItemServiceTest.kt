package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class ItemServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val objectMapper: ObjectMapper, val itemRepository: ItemRepository
) {
    val baseURL: String = "/items/"

    var token: String = ""


    @Nested
    @DisplayName("Create Tests")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostRouteTests {
        @BeforeAll
        fun createTokenForManager() {
            val jwt =
                Jwts.builder().setIssuer("1").setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 10000))
                    .signWith(SignatureAlgorithm.HS512, "dummysecret").compact()

            token = jwt
        }

        @Test
        fun `should throw an error if item name is duplicate`() {
            // For some reason if id is any duplicate value, no exception will be thrown but no data will be added either
            // This leads to the below workaround
            // @TODO fix later
            val item = Item(id = null, name = "TestName", quantity_available = 10, price = 10)

            //when
            itemRepository.deleteByName(item.name)

            val tempItem = itemRepository.save(item)
            item.id = tempItem.id?.plus(1)

            val performPost = mockMvc.post(baseURL) {
                header("Authorization", "Bearer $token")
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
            val price = 2000
            val quantityAvailable = 2000
            itemRepository.deleteByName("NAMEWHICHWILLNEVERBECREATED")

            // given
            val newItem = Item(
                id = null, name = "NAMEWHICHWILLNEVERBECREATED", price = price, quantity_available = quantityAvailable
            )

            // when
            val performPost = mockMvc.post(baseURL) {
                header("Authorization", "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newItem)
            }

            // then
            performPost.andDo { print() }.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }

            // after
            itemRepository.deleteByName("NAMEWHICHWILLNEVERBECREATED")
        }
    }

    @Nested
    @DisplayName("Update Tests")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchRouteTests {
        @BeforeAll
        fun createTokenForManager() {
            val jwt =
                Jwts.builder().setIssuer("1").setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 10000))
                    .signWith(SignatureAlgorithm.HS512, "dummysecret").compact()

            token = jwt
        }

        @Test
        fun `should return 404 and appropriate error if given id is not found`() {
            // given
            val item = Item(id = -1, name = "TestName", quantity_available = 10, price = 10)

            // when
            val performUpdate = mockMvc.patch("$baseURL/-1") {
                header("Authorization", "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(item)
            }

            // then
            performUpdate.andDo { print() }.andExpect {
                status { isNotFound() }
                content {
                    json("{\"status\":404,\"message\":\"No item with given details found\"}")
                }
            }
        }

        @Test
        fun `should update item and return Accepted (204) if request is correct`() {
            // before
            val item = Item(id = null, name = "UpdateName", quantity_available = 10, price = 10)
            itemRepository.save(item)

            // given
            val newItem = Item(id = null, name = "NewUpdatedItem", quantity_available = 20, price = 20)

            // when
            val performUpdate = mockMvc.patch("$baseURL/${item.id}") {
                header("Authorization", "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newItem)
            }

            // then
            performUpdate.andDo { print() }.andExpect {
                status { isNoContent() }
            }

            // after
            itemRepository.delete(item)
            itemRepository.delete(newItem)
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteRouteTests {
        @BeforeAll
        fun createTokenForManager() {
            val jwt =
                Jwts.builder().setIssuer("1").setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 10000))
                    .signWith(SignatureAlgorithm.HS512, "dummysecret").compact()

            token = jwt
        }

        @Test
        fun `should return 404 and appropriate error if given id is not found`() {
            // when
            val performDelete = mockMvc.delete("$baseURL/-1") {
                header("Authorization", "Bearer $token")
            }

            // then
            performDelete.andDo { print() }.andExpect {
                status { isNotFound() }
                content {
                    json("{\"status\":404,\"message\":\"No item with given details found\"}")
                }
            }
        }

        @Test
        fun `should delete item and return Accepted (204) if request is correct`() {
            // before
            val item = Item(id = null, name = "UpdateName", quantity_available = 10, price = 10)
            itemRepository.save(item)

            // when
            val performDelete = mockMvc.delete("$baseURL/${item.id}") {
                header("Authorization", "Bearer $token")
            }

            // then
            performDelete.andDo { print() }.andExpect {
                status { isNoContent() }
            }

            val performGet = mockMvc.get("$baseURL/${item.name}") {
                header("Authorization", "Bearer $token")

            }
            performGet.andDo { print() }.andExpect {
                status { isOk() }
                content {
                    string("")
                }
            }
        }
    }

    @AfterEach
    fun cleanup() {
        itemRepository.deleteByName("UpdateName")
    }
}
