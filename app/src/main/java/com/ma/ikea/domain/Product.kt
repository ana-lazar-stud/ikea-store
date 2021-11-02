package com.ma.ikea.domain

import java.io.Serializable

data class Product (
    var id: String,
    val name: String,
    val description: String,
    val company: String,
    val quantity: Int,
    val category: String,
    val isle: String
) : Serializable