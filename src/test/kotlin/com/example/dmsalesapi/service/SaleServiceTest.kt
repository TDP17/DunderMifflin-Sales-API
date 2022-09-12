package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.ItemQuantityMappingEntity
import com.example.dmsalesapi.model.SalePostEntity
import com.example.dmsalesapi.repository.SaleRepository
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

@SpringBootTest
@AutoConfigureMockMvc
internal class SaleServiceTest @Autowired constructor(
    val mockMvc: MockMvc, val saleRepository: SaleRepository, val objectMapper: ObjectMapper
) {
    val baseURL: String = "/sales/"

    @Nested
    @DisplayName("Create Tests")
    inner class PostRouteTests {
        @Test
        fun `should add entry in sale_item table as well as base sale table`() {
            // when
            val performPost = mockMvc.post(baseURL) {
                contentType = MediaType.APPLICATION_JSON
                content =
                    "{\n" + "\"customer_id\": 5,\"employee_id\": 1,\"itemList\": [{\"id\": 98, \"quantity\": 140}, {\"id\": 99, \"quantity\": 150}]\n" + "}"
            }

            //then
            val result: MvcResult = performPost.andDo { print() }.andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }.andReturn()

            val json: String = result.response.contentAsString
            val createdSaleEntity: SalePostEntity = objectMapper.readValue(json, SalePostEntity::class.java)

            for (entry: ItemQuantityMappingEntity in createdSaleEntity.itemList) {
                try {
                    println("${entry.id} ${entry.quantity} ${createdSaleEntity.employee_id}")
                    val entryInAssociationTable =
                        saleRepository.fetchFromAssociationTable(createdSaleEntity.id!!, entry.id)
                    assertEquals(false, entryInAssociationTable.isEmpty)
                    assertEquals(entryInAssociationTable.get(), entry.quantity)
                } catch (e: Exception) {
                    println(e)
                }
            }

            // after
            saleRepository.deleteFromAssociationTable(createdSaleEntity.id!!)
            saleRepository.deleteById(createdSaleEntity.id!!)
        }
    }
}