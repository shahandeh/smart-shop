package com.example.smartshop.data.model.review

data class Review(
    val id: Int,
    val date_created: String,
    val product_id: Int,
    val review: String,
    val reviewer: String,
    val rating: Int,
)