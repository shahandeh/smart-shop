package com.example.smartshop.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.smartshop.data.model.Product

class ProductDiffCallback: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}