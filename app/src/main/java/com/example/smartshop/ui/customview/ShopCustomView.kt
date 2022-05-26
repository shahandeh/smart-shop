package com.example.smartshop.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieDrawable
import com.example.smartshop.R
import com.example.smartshop.databinding.CustomViewLayoutBinding
import javax.inject.Inject

class ShopCustomView @Inject constructor(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {

    private var binding: CustomViewLayoutBinding

    init {
        val view = inflate(context, R.layout.custom_view_layout, this)
        binding = CustomViewLayoutBinding.bind(view)
    }

    fun onLoading(){
        binding.apply {
            lottie.visible()
            lottie.playAnimation()
            lottie.repeatCount = LottieDrawable.INFINITE
            textView.gone()
            reload.gone()
        }
    }

    fun onSuccess(){
        binding.apply {
            lottie.gone()
            lottie.pauseAnimation()
            textView.gone()
            reload.gone()
        }
    }

    fun onFail(input: String){
        binding.apply {
            lottie.gone()
            lottie.pauseAnimation()
            textView.visible()
            reload.visible()
            textView.text = input
        }
    }

    fun click(fn: () -> Unit){
        binding.reload.setOnClickListener {
            fn()
        }
    }

    private fun View.visible(){
        this.visibility = View.VISIBLE
    }

    private fun View.gone(){
        this.visibility = View.GONE
    }

}