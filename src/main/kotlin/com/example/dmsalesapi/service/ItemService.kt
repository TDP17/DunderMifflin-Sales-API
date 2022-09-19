package com.example.dmsalesapi.service

import com.example.dmsalesapi.exceptions.DuplicateNameException
import com.example.dmsalesapi.exceptions.IdNotFoundException
import com.example.dmsalesapi.exceptions.UnauthorizedException
import com.example.dmsalesapi.model.Item
import com.example.dmsalesapi.repository.ItemRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun verifyRole(acceptedRoles: List<String>, request: HttpServletRequest): Boolean {
        val roleId = request.getAttribute("employee_role_code")

        return acceptedRoles.contains(roleId)
    }

    fun getItems(request: HttpServletRequest): MutableIterable<Item> {
        if (verifyRole(listOf("1", "4", "5"), request)) return itemRepository.findAll()
        else throw UnauthorizedException("Not authorized to view these records")
    }

    fun getItemByName(name: String, request: HttpServletRequest): Item? {
        if (verifyRole(listOf("1", "4", "5"), request)) return itemRepository.findByName(name)
        else throw UnauthorizedException("Not authorized to view these records")
    }

    fun createItem(item: Item, request: HttpServletRequest): Item {
        if (verifyRole(listOf("1", "4", "5"), request)) {
            return try {
                itemRepository.save(item)
            } catch (e: Exception) {
                throw DuplicateNameException("Another paper with the same name exists, please update the entity instead")
            }
        } else throw UnauthorizedException("Not authorized to view these records")
    }

    fun updateItem(id: Int, _name: String, _price: Int, _quantity_available: Int, request: HttpServletRequest): Item {
        if (verifyRole(listOf("1", "4", "5"), request)) {
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
        } else throw UnauthorizedException("Not authorized to view these records")

    }

    fun deleteItem(id: Int, request: HttpServletRequest) {
        if (verifyRole(listOf("1", "4", "5"), request)) {
            return try {
                itemRepository.deleteById(id.toLong())
            } catch (e: Exception) {
                throw IdNotFoundException("No item with given details found")
            }
        } else throw UnauthorizedException("Not authorized to view these records")

    }
}