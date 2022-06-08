package com.example.smartshop.ui.createreview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.smartshop.R
import com.example.smartshop.constant.RULE
import com.example.smartshop.data.CurrentUser.user_name
import com.example.smartshop.databinding.FragmentCreateReviewBinding
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateReviewFragment : Fragment(R.layout.fragment_create_review) {

    private val args by navArgs<CreateReviewFragmentArgs>()
    private val createReviewViewModel by viewModels<CreateReviewViewModel>()
    private lateinit var binding: FragmentCreateReviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateReviewBinding.bind(view)
        createReviewViewModel.productId = args.productId.toString()
        createReviewViewModel.reviewId = args.reviewId.toString()

        binding.submitReview.setOnClickListener { setData() }

        binding.rule.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(RULE)
            startActivity(i)
        }

        binding.slider.addOnChangeListener { _, value, _ ->
            when (value) {
                0.0F -> binding.sliderText.text = ""
                1.0F -> binding.sliderText.text = getString(R.string.very_bad)
                2.0F -> binding.sliderText.text = getString(R.string.bad)
                3.0F -> binding.sliderText.text = getString(R.string.normal)
                4.0F -> binding.sliderText.text = getString(R.string.good)
                5.0F -> binding.sliderText.text = getString(R.string.perfect)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                createReviewViewModel.createReviewResponse.collect {
                    when (it) {
                        ResultWrapper.Loading -> {}
                        is ResultWrapper.Success -> {
                            view.snack(getString(R.string.your_review_with_id) + it.value.id.toString() + getString(
                                R.string.submitted))
                        }
                        is ResultWrapper.Failure -> {
                            view.snack(it.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setData() {
        if (binding.review.text.isNullOrBlank()) binding.root.snack(getString(R.string.fill_star_field))
        else {
            createReviewViewModel.body = binding.review.text.toString()
            createReviewViewModel.negative = binding.negative.text.toString()
            createReviewViewModel.point = binding.slider.value.toInt()
            createReviewViewModel.positive = binding.positive.text.toString()
            createReviewViewModel.suggestMessage = binding.reviewCustom.answer
            createReviewViewModel.suggestTitle = binding.title.text.toString()
            if (!binding.unknown.isChecked) createReviewViewModel.userName = user_name
            else createReviewViewModel.userName = "کاربر ناشناس"
        }
        createReviewViewModel.createReview()
    }
}