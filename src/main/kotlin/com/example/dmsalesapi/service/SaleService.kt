package com.example.dmsalesapi.service

import com.example.dmsalesapi.model.ItemQuantityMappingEntity
import com.example.dmsalesapi.model.Sale
import com.example.dmsalesapi.model.SalePostEntity
import com.example.dmsalesapi.repository.SaleRepository
import org.springframework.stereotype.Service

@Service
class SaleService(private val saleRepository: SaleRepository) {
    fun getSales(): MutableIterable<Sale> = saleRepository.findAll()

    fun createSale(saleEntity: SalePostEntity): SalePostEntity {
        val millis = System.currentTimeMillis()
        val date = java.sql.Date(millis)

        val sale = Sale(saleEntity.id, saleEntity.employee_id, saleEntity.customer_id, date)
        val newSale = saleRepository.save(sale)

        for (entry: ItemQuantityMappingEntity in saleEntity.itemList) {
            saleRepository.insertIntoSaleItems(newSale.id!!, newSale.date!!, entry.quantity, entry.id)
            saleRepository.reduceQuantityOfItem(entry.id, entry.quantity)
        }

        return SalePostEntity(newSale.id, newSale.employee_id, newSale.customer_id, saleEntity.itemList)
    }
}