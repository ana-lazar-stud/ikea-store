package com.ma.ikea.products

import android.app.Application
import androidx.lifecycle.*
import com.ma.ikea.data.local.ProductsDatabase
import com.ma.ikea.data.Product
import com.ma.ikea.data.local.ProductsRepository

class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableCompleted = MutableLiveData<String>().apply { value = null }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    var products: LiveData<List<Product>>
    var error: LiveData<Exception> = mutableException
    var completed: LiveData<String> = mutableCompleted

    private val productsRepository: ProductsRepository

    init {
        val productDao = ProductsDatabase.getDatabase(application).productDao()
        productsRepository = ProductsRepository(productDao)
        products = productsRepository.products
    }
}
