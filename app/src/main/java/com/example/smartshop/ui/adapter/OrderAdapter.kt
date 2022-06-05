package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.data.model.product.ProductInOrder
import com.example.smartshop.databinding.OrderSampleBinding
import com.example.smartshop.ui.order.OrderClickListener
import com.example.smartshop.util.glide
import com.example.smartshop.util.visible

class OrderAdapter(
    private val orderClickListener: OrderClickListener,
) :
    ListAdapter<ProductInOrder, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    inner class OrderViewHolder(private val binding: OrderSampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductInOrder) {
            binding.apply {
                productName.text = product.name
                if (product.image.isNotBlank()) productImage.glide(product.image)
                if (product.regular_price.isNotBlank() && product.sale_price.isNotBlank()) {
                    discountText.visible()
                    discount.text =
                        (product.regular_price.toInt() - product.sale_price.toInt()).toString()
                }
                price.text = product.regular_price
                countNumber.text = product.quantity.toString()
                if (product.quantity == 1) minusCount.setImageResource(R.drawable.ic_delete)
                plusCount.setOnClickListener { orderClickListener.plusCount(product.id) }
                minusCount.setOnClickListener { orderClickListener.minusCount(product.id) }
                root.setOnClickListener { orderClickListener.clickItem(product.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderViewHolder(OrderSampleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class OrderDiffCallback : DiffUtil.ItemCallback<ProductInOrder>() {

    override fun areItemsTheSame(oldItem: ProductInOrder, newItem: ProductInOrder) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ProductInOrder, newItem: ProductInOrder) =
        oldItem == newItem
}