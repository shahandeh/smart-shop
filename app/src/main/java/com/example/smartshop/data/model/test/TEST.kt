package com.example.smartshop.data.model.test

data class TEST(
    val customer_id: Int,
    val date_created: String,
    val discount_total: String,
    val id: Int,
    val line_items: List<LineItem>,
    val number: String,
    val status: String,
    val total: String,
)

data class LineItem(
    val id: Int,
    val name: String,
    val price: Int,
    val product_id: Int,
    val quantity: Int,
    val total: String,
)