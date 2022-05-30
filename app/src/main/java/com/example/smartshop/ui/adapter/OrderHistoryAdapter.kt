package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.Order
import com.example.smartshop.databinding.OrderHistorySampleBinding

class OrderHistoryAdapter: ListAdapter<Order, OrderHistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallback()) {

    inner class OrderHistoryViewHolder(private val binding: OrderHistorySampleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(order: Order){
            binding.apply {
                createDate.text = order.date_created.split("T")[0]
                id.text = order.id.toString()
                orderPrice.text = order.total
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderHistoryViewHolder(OrderHistorySampleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class OrderHistoryDiffCallback: DiffUtil.ItemCallback<Order>(){

    override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem

}