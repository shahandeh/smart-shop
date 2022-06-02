package com.example.smartshop.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.smartshop.data.model.product.Category

class CategoryDiffCallback: DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
}