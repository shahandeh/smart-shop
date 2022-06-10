package com.example.smartshop.ui.reviewlist

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.smartshop.R
import com.example.smartshop.databinding.FragmentReviewListBinding
import com.example.smartshop.safeapi.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewListFragment : Fragment(R.layout.fragment_review_list), IReviewList {

    private val reviewListViewModel by viewModels<ReviewListViewModel>()
    private val args by navArgs<ReviewListFragmentArgs>()
    private lateinit var binding: FragmentReviewListBinding
    private lateinit var reviewListAdapter: ReviewListAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewListBinding.bind(view)
        reviewListAdapter = ReviewListAdapter(this)
        binding.recyclerView.adapter = reviewListAdapter
        reviewListViewModel.productId = args.productId.toInt()
        reviewListViewModel.getProductReviewList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {

                launch {
                    reviewListViewModel.reviewList.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                reviewListAdapter.submitList(it.value)
                            }
                            is ResultWrapper.Failure -> {
                                binding.customView.apply {
                                    onFail(it.message.toString())
                                    click { reviewListViewModel.getProductReviewList() }
                                }
                            }
                        }
                    }
                }

                launch {
                    reviewListViewModel.removeReview.collect {
                        when (it) {
                            ResultWrapper.Loading -> {
                                binding.customView.onLoading()
                            }
                            is ResultWrapper.Success -> {
                                binding.customView.onSuccess()
                                reviewListViewModel.getProductReviewList()
                            }
                            is ResultWrapper.Failure -> {
                                binding.customView.onFail(it.message.toString())
                                binding.customView.click { reviewListViewModel.removeReview() }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun delete(reviewId: Int) {
        reviewListViewModel.reviewId = reviewId
        reviewListViewModel.removeReview()
    }

    override fun edit(reviewId: Int) {
        val action = ReviewListFragmentDirections.actionGlobalCreateReviewFragment(null, reviewId.toString())
        findNavController().navigate(action)
    }
}