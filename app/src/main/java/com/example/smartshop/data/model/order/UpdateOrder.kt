package com.example.smartshop.data.model.order

data class UpdateOrder(
    val line_items: List<LineItem>,
    val status: String = "pending"
)