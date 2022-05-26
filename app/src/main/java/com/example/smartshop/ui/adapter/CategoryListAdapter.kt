package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.Category
import com.example.smartshop.databinding.CategoryListSampleBinding
import com.example.smartshop.util.glide

class CategoryListAdapter(private val fn: (id: Int) -> Unit): ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    inner class CategoryViewHolder(private val binding: CategoryListSampleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.apply {
                categoryImage.glide(category.image.src, shimmer)
                categoryName.text = category.name
                categoryCount.text = category.count.toString()
                root.setOnClickListener { fn(category.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(CategoryListSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}