package com.ma.ikea.repositories

import com.ma.ikea.domain.Product

object ProductsRepository {
    private var products = mutableListOf<Product>()

    private val count = 10

    init {
        for (i in 1..count) {
            saveProduct(createProduct(i))
        }
    }

    private fun createProduct(position: Int): Product {
        return Product(position.toString(), "ScaunNumber" + position, "Description " + position, "Company " + position, position, "Category " + position, position.toString())
    }

    fun saveProduct(product: Product) {
        if (product.id == "null") {
            product.id = generateId()
        }

        products.add(product)
    }

    fun updateProduct(product: Product) {
        val index = products.indexOfFirst{ p-> p.id == product.id }
        products[index] = product
    }

    fun removeProduct(id: String) {
        products.removeIf { p -> p.id == id }
    }

    fun getProducts(): List<Product> {
        return products
    }

    private fun generateId(): String {
        var id = 0
        while (products.indexOfFirst{ p -> p.id == id.toString() } != -1) {
            id += 1
        }
        return id.toString()
    }
}