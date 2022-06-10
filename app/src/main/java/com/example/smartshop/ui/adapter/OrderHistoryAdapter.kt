package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.databinding.OrderHistorySampleBinding

class OrderHistoryAdapter :
    ListAdapter<GetOrder, OrderHistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallback()) {

    inner class OrderHistoryViewHolder(private val binding: OrderHistorySampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: GetOrder) {
            binding.apply {
                createDate.text = order.date_created.split("T")[0]
                total.text = order.total
                id.text = order.id.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderHistoryViewHolder(OrderHistorySampleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class OrderHistoryDiffCallback : DiffUtil.ItemCallback<GetOrder>() {

    override fun areItemsTheSame(oldItem: GetOrder, newItem: GetOrder) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GetOrder, newItem: GetOrder) = oldItem == newItem

}