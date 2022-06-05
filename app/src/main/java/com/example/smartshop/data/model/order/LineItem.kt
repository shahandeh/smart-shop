package com.example.smartshop.data.model.order

data class LineItem(
    val id: Int,
    val product_id: Int,
    val quantity: Int,
    val total: String = "0"
)