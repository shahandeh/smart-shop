package com.example.smartshop.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.smartshop.data.model.Product

class HomeDiffCallback: DiffUtil.ItemCallback<DataModel>() {

    override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel) = oldItem == newItem

    override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel) = oldItem == newItem
}