package com.example.smartshop.data.model.order

data class CreateOrderResponse(
    val date_created: String,
    val id: Int,
    val line_items: List<LineItem>,
    val status: String
)