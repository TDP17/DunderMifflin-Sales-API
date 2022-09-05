package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.DuplicateNameException
import com.example.dmsalesapi.exceptions.IdNotFoundException
import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.Exception

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getItems(): MutableIterable<Item> = itemRepository.findAll()

    fun getItemByName(name: String): Item? = itemRepository.findByName(name)

    fun createItem(item: Item): Item {
        return try {
            itemRepository.save(item)
        } catch (e: Exception) {
            throw DuplicateNameException("Another paper with the same name exists, please update the entity instead")
        }
    }

    fun updateItem(id: Int, _name: String, _price: Int, _quantity_available: Int): Any {
        return try {
            val item: Optional<Item> = itemRepository.findById(id.toLong())
            if (item.isEmpty) {
                throw IdNotFoundException("No item with given details found")
            } else {
                val newItem: Item = item.get()
                newItem.name = _name
                newItem.price = _price
                newItem.quantity_available = _quantity_available

                itemRepository.save(newItem)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}