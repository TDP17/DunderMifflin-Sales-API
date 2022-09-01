package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getItems(): MutableIterable<Item> = itemRepository.findAll();

    fun getItemByName(name: String): Item? = itemRepository.findByName(name);

    fun createItem(item: Item): Any {
        return if (itemRepository.findByName(item.name) == null)
            itemRepository.save(item)
        else {
            "Error, paper with name ${item.name} already exists, please update the record instead"
        }
    }
}