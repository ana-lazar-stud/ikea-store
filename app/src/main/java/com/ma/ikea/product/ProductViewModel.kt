package com.ma.ikea.product

import android.app.Application
import androidx.lifecycle.*
import com.ma.ikea.R
import com.ma.ikea.core.Result
import com.ma.ikea.data.local.ProductsDatabase
import com.ma.ikea.data.Product
import com.ma.ikea.data.local.ProductsRepository
import com.ma.ikea.isStringValid
import com.ma.ikea.logd
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableProductFormState = MutableLiveData<ProductFormState>()
    private val mutableException = MutableLiveData<Exception>().apply { value = null }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }

    val productFormState: LiveData<ProductFormState> = mutableProductFormState
    var error: LiveData<Exception> = mutableException
    var completed: LiveData<Boolean> = mutableCompleted

    private val productsRepository: ProductsRepository

    init {
        val productDao = ProductsDatabase.getDatabase(application).productDao()
        productsRepository = ProductsRepository(productDao)
    }

    fun getById(id: Int): LiveData<Product> {
        logd("getById")
        return productsRepository.getById(id)
    }

    fun saveOrUpdate(product: Product) {
        viewModelScope.launch {
            val result: Result<Product>
            mutableException.value = null
            productDataCheck(product)
            if (productFormState.value?.isDataValid == true) {
                if (product._id === null) {
                    result = productsRepository.save(product)
                } else {
                    result = productsRepository.update(product)
                }
                when (result) {
                    is Result.Success -> {
                        logd("update successful")
                        mutableCompleted.value = true
                    }
                    is Result.Error -> {
                        logd("update unsuccessful")
                        mutableException.value = result.exception
                    }
                }
            }
        }
    }

    fun remove(id: Int) {
        viewModelScope.launch {
            mutableException.value = null
            when (val result = productsRepository.remove(id)) {
                is Result.Success -> {
                    logd("remove successful")
                    mutableCompleted.value = true
                }
                is Result.Error -> {
                    logd("remove unsuccessful")
                    mutableException.value = result.exception
                }
            }
        }
    }

    private fun productDataCheck(product: Product) {
        var nameError: Int? = null
        var descriptionError: Int? = null
        var companyError: Int? = null
        var categoryError: Int? = null
        var isleError: Int? = null
        if (!isStringValid(product.name)) {
            nameError = R.string.invalid_name
        }
        if (!isStringValid(product.description)) {
            descriptionError = R.string.invalid_description
        }
        if (!isStringValid(product.company)) {
            companyError = R.string.invalid_description
        }
        if (!isStringValid(product.category)) {
            categoryError = R.string.invalid_category
        }
        if (!isStringValid(product.isle)) {
            isleError = R.string.invalid_isle
        }
        val valid = nameError === null && descriptionError === null && companyError === null && categoryError === null && isleError === null
        mutableProductFormState.value = ProductFormState(nameError, descriptionError, companyError, categoryError, isleError, valid)
    }
}