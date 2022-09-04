package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.DuplicateNameException
import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import kotlin.Exception

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getItems(): MutableIterable<Item> = itemRepository.findAll()

    fun getItemByName(name: String): Item? = itemRepository.findByName(name)

    fun createItem(item: Item): Item {
        return try {
            itemRepository.save(item)
        }
        catch (e: Exception) {
            throw DuplicateNameException("Another paper with the same name exists, please update the entity instead")
        }
    }
}