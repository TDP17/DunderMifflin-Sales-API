package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class ItemController(private val itemService: ItemService) {
    @GetMapping("/items")
    fun getItems(): MutableIterable<Item> = itemService.getItems()

    @GetMapping("/items/{name}")
    fun getItemByName(@PathVariable name: String): Item? = itemService.getItemByName(name)

    @PostMapping("/items")
    @ResponseBody
    fun createItem(@RequestBody item: Item): ResponseEntity<Item> {
        val newItem: Item = itemService.createItem(item)
        return ResponseEntity<Item>(
            newItem,
            HttpStatus.CREATED
        )
    }

    @PatchMapping("/items/{id}")
    @ResponseBody
    fun updateItem(@PathVariable id: Int, @RequestBody item: Item): ResponseEntity<String> {
        itemService.updateItem(id, item.name, item.price, item.quantity_available)
        return ResponseEntity<String>(
            "Updated successfully",
            HttpStatus.NO_CONTENT
        )
    }
}