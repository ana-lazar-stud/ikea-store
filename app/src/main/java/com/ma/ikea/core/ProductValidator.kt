package com.ma.ikea.core

import com.ma.ikea.data.Product

object ProductValidator {
    fun validate(product: Product): String {
        val errors = arrayListOf<String>()
        if (product.name == "") {
            errors.add("Name cannot be empty")
        }
        if (product.category == "") {
            errors.add("Category cannot be empty")
        }
        if (product.company == "") {
            errors.add("Company cannot be empty")
        }
        if (product.description == "") {
            errors.add("Description cannot be empty")
        }
        if (product.quantity == 0) {
            errors.add("Quantity cannot be zero")
        }
        if (product.isle == "") {
            errors.add("Isle cannot be empty")
        }
        return errors.joinToString(separator = "\n")
    }
}