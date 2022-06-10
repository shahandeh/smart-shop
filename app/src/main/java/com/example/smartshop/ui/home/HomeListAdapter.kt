package com.example.smartshop.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.util.glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class HomeListAdapter(
    private var homeClickListener: HomeClickListener
) : ListAdapter<DataModel, HomeListAdapter.HomeViewHolder>(HomeDiffCallback()) {

    inner class HomeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: DataModel){
            when(dataModel) {
                is DataModel.Header -> bindHeader(dataModel)
                is DataModel.Data -> bindData(dataModel)
                is DataModel.Footer -> bindFooter(dataModel)
            }
        }

        private fun bindHeader(dataModel: DataModel.Header){
            itemView.findViewById<MaterialTextView>(R.id.title).text = dataModel.title
            itemView.setOnClickListener { homeClickListener.moreProduct(dataModel.order) }
        }

        private fun bindData(dataModel: DataModel.Data){
            val shimmer = itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer)
            if (dataModel.product.images.isNotEmpty()) itemView.findViewById<ShapeableImageView>(R.id.product_list_sample_image).glide(dataModel.product.images[0].src, shimmer)
            itemView.findViewById<MaterialTextView>(R.id.product_list_sample_name).text = dataModel.product.name
            itemView.findViewById<MaterialTextView>(R.id.product_list_sample_regular_price).text = dataModel.product.regular_price
            itemView.setOnClickListener { homeClickListener.product(dataModel.product.id.toString()) }
        }

        private fun bindFooter(dataModel: DataModel.Footer){
            itemView.setOnClickListener { homeClickListener.moreProduct(dataModel.order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layout = when(viewType){
            HEADER -> R.layout.home_header_sample
            DATA -> R.layout.home_product_list_sample
            FOOTER -> R.layout.home_footer_sample
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return HomeViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataModel.Header -> HEADER
            is DataModel.Data -> DATA
            is DataModel.Footer -> FOOTER
        }
    }

    companion object {
        private const val HEADER = 0
        private const val DATA = 1
        private const val FOOTER = 2
    }

}