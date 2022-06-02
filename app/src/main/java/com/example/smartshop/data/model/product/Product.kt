package com.example.smartshop.data.model.product

data class Product(
    val id: Int,
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
    val src: String
)

data class Tag(
    val name: String
)

data class Category(
    val id: Int,
    val image: Image,
    val name: String,
    val count: Int,
)