package com.example.smartshop.data.model.order

data class GetOrder(
    val date_created: String,
    val id: Int,
    val line_items: List<LineItem>,
    val status: String,
    val discount_total: String,
    val total: String,
)