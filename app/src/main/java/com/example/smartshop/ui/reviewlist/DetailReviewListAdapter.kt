package com.example.smartshop.ui.reviewlist

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartshop.R
import com.example.smartshop.util.cleaner
import com.example.smartshop.util.timeCalc
import com.google.android.material.textview.MaterialTextView

@RequiresApi(Build.VERSION_CODES.N)
class DetailReviewListAdapter (private val fn: (productId: String) -> Unit):
    ListAdapter<ReviewDataModel, DetailReviewListAdapter.DetailReviewViewHolder>(
        DetailReviewCallback()) {

    inner class DetailReviewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: ReviewDataModel) {
            when (item) {
                is ReviewDataModel.Data -> data(item)
                is ReviewDataModel.Footer -> footer(item)
            }
        }

        fun data(dataModel: ReviewDataModel.Data) {
            itemView.apply {
                findViewById<MaterialTextView>(R.id.review_body).text = dataModel.review.cleaner()
                findViewById<MaterialTextView>(R.id.review_reviewer).text = dataModel.reviewer
                findViewById<MaterialTextView>(R.id.review_date).text =
                    dataModel.date_created.timeCalc()
                itemView.setOnClickListener { fn(dataModel.product_id.toString()) }
            }
        }

        private fun footer(dataModel: ReviewDataModel.Footer) {
            itemView.setOnClickListener { fn(dataModel.product_id.toString()) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailReviewViewHolder {
        val layout = when (viewType) {
            DATA -> R.layout.review_sample
            FOOTER -> R.layout.reveiw_footer_sample
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
        return DetailReviewViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {
            is ReviewDataModel.Data -> DATA
            is ReviewDataModel.Footer -> FOOTER
        }

    companion object {
        private const val DATA = 0
        private const val FOOTER = 1
    }

}

class DetailReviewCallback : DiffUtil.ItemCallback<ReviewDataModel>() {

    override fun areItemsTheSame(oldItem: ReviewDataModel, newItem: ReviewDataModel) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: ReviewDataModel, newItem: ReviewDataModel) =
        oldItem == newItem
}