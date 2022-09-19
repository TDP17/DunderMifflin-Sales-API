package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping
class ItemController(private val itemService: ItemService) {
    @GetMapping("/items")
    fun getItems(request: HttpServletRequest): MutableIterable<Item> = itemService.getItems(request)

    @GetMapping("/items/{name}")
    fun getItemByName(@PathVariable name: String, request: HttpServletRequest): Item? =
        itemService.getItemByName(name, request)

    @PostMapping("/items")
    @ResponseBody
    fun createItem(@RequestBody item: Item, request: HttpServletRequest): ResponseEntity<Item> {
        val newItem: Item = itemService.createItem(item, request)
        return ResponseEntity<Item>(
            newItem, HttpStatus.CREATED
        )
    }

    @PatchMapping("/items/{id}")
    @ResponseBody
    fun updateItem(
        @PathVariable id: Int, @RequestBody item: Item, request: HttpServletRequest
    ): ResponseEntity<String> {
        itemService.updateItem(id, item.name, item.price, item.quantity_available, request)
        return ResponseEntity<String>(
            "Updated successfully", HttpStatus.NO_CONTENT
        )
    }

    @DeleteMapping("items/{id}")
    @ResponseBody
    fun deleteItem(@PathVariable id: Int, request: HttpServletRequest): ResponseEntity<String> {
        itemService.deleteItem(id, request)
        return ResponseEntity<String>(
            "Deleted successfully", HttpStatus.NO_CONTENT
        )
    }
}