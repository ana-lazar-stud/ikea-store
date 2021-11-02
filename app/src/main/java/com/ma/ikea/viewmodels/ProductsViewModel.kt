package com.ma.ikea.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ma.ikea.domain.Product
import com.ma.ikea.repositories.ProductsRepository

class ProductsViewModel : ViewModel() {
    private val mutableProducts = MutableLiveData<List<Product>>().apply { value = ProductsRepository.getProducts() }

    val products: LiveData<List<Product>> = mutableProducts

    init {
        mutableProducts.value = ProductsRepository.getProducts()
    }

    fun saveProduct(product: Product) {
        ProductsRepository.saveProduct(product)
    }

    fun updateProduct(product: Product) {
        ProductsRepository.updateProduct(product)
    }

    fun removeProduct(id: String) {
        ProductsRepository.removeProduct(id)
    }
}
