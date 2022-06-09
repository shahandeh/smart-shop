package com.example.smartshop.data.model.order

data class UpdateOrder(
    val line_items: List<LineItem> = emptyList(),
    val status: String = "pending",
    val coupon_lines: List<Coupon> = emptyList(),
)

data class Coupon(
    val code: String,
    val amount: String,
)