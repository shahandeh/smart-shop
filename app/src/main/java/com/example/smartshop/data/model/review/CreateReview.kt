package com.example.smartshop.data.model.review

data class CreateReview(
    val product_id: Int,
    val review: String,
    val reviewer: String,
    val reviewer_email: String,
    val rating: Int,
)