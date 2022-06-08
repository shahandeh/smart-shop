package com.example.smartshop.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieDrawable
import com.example.smartshop.R
import com.example.smartshop.databinding.CustomViewLayoutBinding
import com.example.smartshop.databinding.ReviewCustomViewBinding
import com.example.smartshop.util.gone
import com.example.smartshop.util.visible
import javax.inject.Inject

class ReviewCustomView @Inject constructor(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {

    private var binding: ReviewCustomViewBinding

    var answer = ""

    init {
        val view = inflate(context, R.layout.review_custom_view, this)
        binding = ReviewCustomViewBinding.bind(view)
        binding.notKnowUnselect.setOnClickListener {
            binding.notKnowUnselect.gone()
            binding.notSuggestUnselect.visible()
            binding.suggestUnselect.visible()
            answer = "مطمئن نیستم"
        }

        binding.notSuggestUnselect.setOnClickListener {
            binding.notSuggestUnselect.gone()
            binding.notKnowUnselect.visible()
            binding.suggestUnselect.visible()
            answer = "توصیه نمی‌کنم"
        }

        binding.suggestUnselect.setOnClickListener {
            binding.suggestUnselect.gone()
            binding.notKnowUnselect.visible()
            binding.notSuggestUnselect.visible()
            answer = "توصیه می‌کنم"
        }

        binding.notKnow.setOnClickListener {
            binding.notKnowUnselect.visible()
            answer = ""
        }

        binding.notSuggest.setOnClickListener {
            binding.notSuggestUnselect.visible()
            answer = ""
        }

        binding.suggestSelect.setOnClickListener {
            binding.suggestUnselect.visible()
            answer = ""
        }
    }

//    fun notKnowSelected() {
//        binding.notKnowUnselect.setOnClickListener {
//
//        }
//        if (notKnowSelected) {
//            notKnowSelected = false
//            binding.notKnowUnselect.visible()
//        } else {
//            notKnowSelected = true
//            binding.notKnowUnselect.gone()
//        }
//    }

    /*fun onLoading(){
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
    }*/

}