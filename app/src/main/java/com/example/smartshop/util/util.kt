package com.example.smartshop.util

import android.graphics.drawable.Drawable
import android.view.View
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