package com.example.smartshop.ui.reviewlist

sealed class ReviewDataModel {

    data class Data (
        val date_created: String,
        val product_id: Int,
        val review: String,
        val reviewer: String,
    ): ReviewDataModel()

    data class Footer(
        val product_id: Int
    ): ReviewDataModel()

}
