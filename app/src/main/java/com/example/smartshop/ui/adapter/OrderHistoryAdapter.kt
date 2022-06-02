package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.data.model.test.TEST
import com.example.smartshop.databinding.OrderHistorySampleBinding

class OrderHistoryAdapter: ListAdapter<TEST, OrderHistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallback()) {

    inner class OrderHistoryViewHolder(private val binding: OrderHistorySampleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(order: TEST){
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

class OrderHistoryDiffCallback: DiffUtil.ItemCallback<TEST>(){

    override fun areItemsTheSame(oldItem: TEST, newItem: TEST) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TEST, newItem: TEST) = oldItem == newItem

}