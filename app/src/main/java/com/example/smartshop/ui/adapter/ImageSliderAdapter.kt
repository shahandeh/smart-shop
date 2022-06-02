package com.example.smartshop.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.smartshop.R
import com.example.smartshop.util.glide
import com.google.android.material.imageview.ShapeableImageView

class ImageSliderAdapter(
    private val imageList: MutableList<String>,
    private val viewPager2: ViewPager2,
) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(url: String) {
            itemView.findViewById<ShapeableImageView>(R.id.container).glide(url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_slider_sample, parent, false)
    )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
        if (position == imageList.size - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}