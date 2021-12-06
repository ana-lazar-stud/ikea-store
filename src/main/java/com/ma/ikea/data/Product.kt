package com.ma.ikea.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val _id: Int? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "company") var company: String,
    @ColumnInfo(name = "quantity") var quantity: Int,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "isle") var isle: String
): Serializable {
    constructor():this(null,"","", "", 0, "", "")
}
