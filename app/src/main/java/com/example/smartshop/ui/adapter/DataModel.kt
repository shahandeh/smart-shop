package com.example.smartshop.ui.adapter

import com.example.smartshop.data.model.product.Product

sealed class DataModel {
    data class Header(
        val title: String,
        val order: String
    ): DataModel()

    data class Data(
        val product: Product
    ): DataModel()

    data class Footer(
        val order: String
    ): DataModel()
}
