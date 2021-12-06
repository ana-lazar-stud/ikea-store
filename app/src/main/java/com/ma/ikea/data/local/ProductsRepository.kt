package com.ma.ikea.data.local

import androidx.lifecycle.LiveData
import com.ma.ikea.core.Result
import com.ma.ikea.data.Product
import com.ma.ikea.logd

class ProductsRepository(private val productDao: ProductDao) {
    val products = productDao.getAll()

    fun getById(id: Int): LiveData<Product> {
        return productDao.getById(id)
    }

    suspend fun save(product: Product): Result<Product> {
        return try {
            productDao.insert(product)
            logd("insert successful")
            Result.Success(product)
        } catch (e: Exception) {
            logd(e.message)
            Result.Error(e)
        }
    }

    suspend fun update(product: Product): Result<Product> {
        return try {
            productDao.update(product)
            logd("update successful")
            Result.Success(product)
        } catch (e: Exception) {
            logd(e.message)
            Result.Error(e)
        }
    }

    suspend fun remove(id: Int): Result<Int> {
        return try {
            val row = productDao.delete(id)
            logd("delete successful")
            Result.Success(row)
        } catch (e: Exception) {
            logd(e.message)
            Result.Error(e)
        }
    }
}