package com.example.smartshop.data.model.order

data class GetOrder(
    val id: Int,
    val total: String,
    val status: String,
    val date_created: String,
    val line_items: List<LineItem>,
)

data class LineItem(
    val id: Int,
    val name: String,
    val product_id: Int,
    val quantity: Int,
    val total: Int,
)