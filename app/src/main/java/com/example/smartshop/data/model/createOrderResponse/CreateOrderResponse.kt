package com.example.smartshop.data.model.createOrderResponse

data class CreateOrderResponse(
    val date_created: String,
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