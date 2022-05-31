package com.example.smartshop.data.model

data class Order(
    val id: Int,
    val total: String,
    val status: String,
    val date_created: String,
    val line_items: List<LineItem>,
)

data class LineItem(
    val id: Int
)