package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class ItemController(private val itemRepository: ItemRepository) {
    @GetMapping("/items")
    fun getItems(): List<Item> = itemRepository.findAll()
}