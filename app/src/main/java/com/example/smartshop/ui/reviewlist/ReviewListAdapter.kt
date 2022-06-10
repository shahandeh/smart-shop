package com.example.smartshop.ui.reviewlist

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.data.CurrentUser.email
import com.example.smartshop.data.model.review.Review
import com.example.smartshop.databinding.ReviewListSampleBinding
import com.example.smartshop.util.cleaner
import com.example.smartshop.util.timeCalc
import com.example.smartshop.util.visible

@RequiresApi(Build.VERSION_CODES.N)
class ReviewListAdapter(private val iReviewList: IReviewList) : ListAdapter<Review, ReviewListAdapter.ReviewListViewHolder>(
    ReviewListDiffCallback()) {

    inner class ReviewListViewHolder(private val binding: ReviewListSampleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            val pointColor = when {
                review.rating > 3 -> Color.GREEN
                review.rating > 1 -> Color.YELLOW
                else -> Color.RED
            }
            binding.apply {
                pointCardView.background.setTint(pointColor)
                point.text = review.rating.toDouble().toString()
                reviewTime.text = review.date_created.timeCalc()
                reviewer.text = review.reviewer
                reviewBody.text = review.review.cleaner()
                if (email == review.reviewer_email) {
                    delete.visible()
                    edit.visible()
                }
                delete.setOnClickListener { iReviewList.delete(review.id) }
                edit.setOnClickListener { iReviewList.edit(review.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewListViewHolder(ReviewListSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

private class ReviewListDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Review, newItem: Review) =
        oldItem == newItem
}