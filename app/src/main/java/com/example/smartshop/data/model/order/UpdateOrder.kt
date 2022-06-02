package com.example.smartshop.data.model.order

class UpdateOrder(
    val line_items: List<UpdateLineItem>,
    val status: String = "pending"
)

data class UpdateLineItem(
    val id: Int,
    val product_id: Int,
    val quantity: Int,
)