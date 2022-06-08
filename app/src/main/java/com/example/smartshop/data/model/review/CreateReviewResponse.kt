package com.example.smartshop.data.model.review

data class CreateReviewResponse(
    val date_created: String,
    val id: Int,
    val product_id: Int,
    val rating: Int,
    val review: String,
    val reviewer: String,
    val reviewer_email: String,
)