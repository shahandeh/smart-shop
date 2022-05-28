package com.example.smartshop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.Product
import com.example.smartshop.databinding.ProductListSampleBinding
import com.example.smartshop.util.glide

class ProductListAdapter(
    private val fn: (id: String) -> Unit
): ListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(private val binding: ProductListSampleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                productImage.glide(product.images[0].src, shimmer)
                productName.text = product.name
                if (product.sale_price.isBlank() || product.sale_price == product.regular_price) {
                    productRegularPrice.text = product.regular_price
                    productPrice.visibility = View.GONE
                    productPriceIrr.visibility = View.GONE
                } else {
                    productRegularPrice.text = product.sale_price
                    productPrice.text = product.regular_price
                }
                productRatingBar.rating = product.average_rating.toFloat()
                root.setOnClickListener { fn(product.id.toString()) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(ProductListSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ProductDiffCallback: DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}