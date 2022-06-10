package com.example.smartshop.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.model.product.Image
import com.example.smartshop.databinding.ImageListSampleBinding
import com.example.smartshop.util.glide

class ImageRecyclerAdapter(
    private val item: List<Image>,
) : RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ImageListSampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {
            binding.imageView.glide(image.src)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImageViewHolder(ImageListSampleBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount() = item.size

}