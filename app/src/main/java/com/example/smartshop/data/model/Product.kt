package com.example.smartshop.data.model

data class Product(
    val name: String,
    val description: String,
    val images: List<Image>,
    val categories: List<Category>,
    val tags: List<Tag>,
    val regular_price: String,
    val sale_price: String,
    val average_rating: String,
    val rating_count: Int,
    val date_created: String,
    val date_modified: String,
    val short_description: String,
)

data class Image(
    val src: String,
)

data class Category(
    val name: String,
)

data class Tag(
    val name: String,
)