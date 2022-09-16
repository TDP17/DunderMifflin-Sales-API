package com.example.dmsalesapi.controller

import com.example.dmsalesapi.model.ExtendedSale
import com.example.dmsalesapi.model.Sale
import com.example.dmsalesapi.model.SalePostEntity
import com.example.dmsalesapi.service.SaleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class SaleController(private val saleService: SaleService) {
    @GetMapping("/sales")
    fun getSales(): MutableIterable<Sale> = saleService.getSales()

    @GetMapping("sales/{id}")
    fun getExtendedSale(@PathVariable id: Int): MutableList<ExtendedSale> = saleService.getExtendedSale(id)

    @PostMapping("/sales")
    @ResponseBody
    fun createSale(@RequestBody sale: SalePostEntity): ResponseEntity<SalePostEntity> {
        val newSale: SalePostEntity = saleService.createSale(sale)
        return ResponseEntity<SalePostEntity>(
            newSale, HttpStatus.CREATED
        )
    }
}