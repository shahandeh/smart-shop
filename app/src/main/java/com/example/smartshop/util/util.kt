package com.example.smartshop.util

import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

fun ShapeableImageView.glide(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

fun ShapeableImageView.glide(url: String, shimmerFrameLayout: ShimmerFrameLayout) {
    shimmerFrameLayout.startShimmer()
    Glide.with(this)
        .load(url)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.hideShimmer()
                return false
            }
        })
        .into(this)
}

fun String.cleaner(): String {
    return this
        .replace("</", "")
        .replace("/>", "")
        .replace("br", "")
        .replace("p", "")
        .replace(">", "")
        .replace("<", "")
        .replace("_", " ")
        .replace("-", " ")
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun ViewModel.launch(fn: suspend () -> Unit) {
    this.viewModelScope.launch {
        fn()
    }
}

fun View.snack(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

@RequiresApi(Build.VERSION_CODES.N)
fun String.timeCalc(): String{
    val temp: String
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date())
    val currentDataYear = currentDate.split("-")[0].toInt()
    val currentDataMount = currentDate.split("-")[1].toInt()
    val currentDataDay = currentDate.split("-")[2].toInt()
    val inputDate = this.split("T")[0]
    val inputDateYear = inputDate.split("-")[0].toInt()
    val inputDateMount = inputDate.split("-")[1].toInt()
    val inputDateDay = inputDate.split("-")[2].toInt()

    temp = when {
        currentDataYear > inputDateYear -> (currentDataYear - inputDateYear).toString() + " سال پیش"
        currentDataMount > inputDateMount -> (currentDataMount - inputDateMount).toString() + " ماه پیش"
        else -> (currentDataDay - inputDateDay).toString() + " روز پیش"
    }
    return temp
}

fun log(input: String){
    Log.d("majid", "log: $input")
}