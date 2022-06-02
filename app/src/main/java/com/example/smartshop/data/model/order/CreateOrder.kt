package com.example.smartshop.data.model.order

class CreateOrder(
    val line_items: List<CreateLineItem>,
)

data class CreateLineItem(
    val product_id: Int,
    val quantity: Int,
)