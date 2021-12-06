package com.ma.ikea.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ma.ikea.data.Product

@Dao
interface ProductDao {
    @Query("SELECT * from products ORDER BY name ASC")
    fun getAll(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE _id=:id ")
    fun getById(id: Int): LiveData<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(product: Product)

    @Query("DELETE FROM products WHERE _id=:id")
    suspend fun delete(id: Int): Int
}
