package com.example.smartshop.data.model.order

data class CreateOrder(
    val line_items: List<CreateOrderLineItem>,
)

data class CreateOrderLineItem(
    val product_id: Int,
    val quantity: Int,
)