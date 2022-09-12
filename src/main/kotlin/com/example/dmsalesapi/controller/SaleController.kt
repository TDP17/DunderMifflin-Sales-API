package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.ItemQuantityMappingEntity
import com.example.dmsalesapi.model.Sale
import com.example.dmsalesapi.model.SalePostEntity
import com.example.dmsalesapi.service.SaleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.reflect.typeOf

@RestController
@RequestMapping
class SaleController(private val saleService: SaleService) {
    @GetMapping("/sales")
    fun getItems(): MutableIterable<Sale> = saleService.getSales()

//    @GetMapping("/items/{name}")
//    fun getItemByName(@PathVariable name: String): Item? = itemService.getItemByName(name)

    @PostMapping("/sales")
    @ResponseBody
    fun createItem(@RequestBody sale: SalePostEntity): ResponseEntity<SalePostEntity> {
        val newSale: SalePostEntity = saleService.createSale(sale)
        return ResponseEntity<SalePostEntity>(
            newSale, HttpStatus.CREATED
        )
    }
}