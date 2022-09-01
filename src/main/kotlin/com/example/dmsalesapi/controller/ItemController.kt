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
    fun createItem(@RequestBody item: Item): Any {
        val response: Any = itemService.createItem(item);
        return if (response is Item) {
            ResponseEntity<String>("Item created $response", HttpStatus.CREATED)
        } else {
            ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}