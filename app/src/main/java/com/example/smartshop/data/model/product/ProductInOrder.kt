package com.example.smartshop.data.model.product

data class ProductInOrder(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val sale_price: String,
    val regular_price: String,
    val quantity: Int
)
