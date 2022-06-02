package com.example.smartshop.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class HomeDiffCallback: DiffUtil.ItemCallback<DataModel>() {

    override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel) = oldItem == newItem

    override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel) = oldItem == newItem
}