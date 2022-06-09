package com.example.smartshop.data.model.order

data class UpdateOrderResponse(
    val date_created: String,
    val id: Int,
    val total: String,
    val status: String,
    val discount_total: String,
    val line_items: List<LineItem>,
    val coupon_lines: List<Coupon> = emptyList(),
)