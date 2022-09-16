package com.example.dmsalesapi.repository

import com.example.dmsalesapi.model.ExtendedSale
import com.example.dmsalesapi.model.Sale
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.util.*

interface SaleRepository : CrudRepository<Sale, Int> {
    @Query(
        value = "select sale_id, item_name from extended_sale where sale_id=:sale_id",
        nativeQuery = true
    )
    fun findExtendedSale(
        @Param("sale_id") sale_id: Int,
    ): MutableList<Optional<ExtendedSale>>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO sale_item(sale_id, sale_date, quantity, item_id) VALUES (:sale_id, :sale_date, :quantity, :item_id)",
        nativeQuery = true
    )
    fun insertIntoSaleItems(
        @Param("sale_id") sale_id: Int,
        @Param("sale_date") sale_date: Date,
        @Param("quantity") quantity: Int,
        @Param("item_id") item_id: Int
    )

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE item SET quantity_available=quantity_available-:quantity WHERE id=:item_id", nativeQuery = true
    )
    fun reduceQuantityOfItem(
        @Param("item_id") item_id: Int, @Param("quantity") quantity: Int
    )

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM sale_item WHERE sale_id=:sale_id", nativeQuery = true
    )
    fun deleteFromAssociationTable(
        @Param("sale_id") sale_id: Int
    )

    @Query(
        value = "SELECT quantity FROM sale_item WHERE sale_id=:sale_id and item_id=:item_id", nativeQuery = true
    )
    fun fetchFromAssociationTable(@Param("sale_id") employee_id: Int, @Param("item_id") item_id: Int): Optional<Int>
}

interface ExtendedSaleRepository : CrudRepository<ExtendedSale, Int> {
    @Query(
        value = "select * from extended_sale where sale_id=:sale_id",
        nativeQuery = true
    )
    fun findExtendedSale(
        @Param("sale_id") sale_id: Int,
    ): MutableList<ExtendedSale>
}