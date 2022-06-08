package com.example.smartshop.ui.reviewlist

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.review.RemoveReviewResponse
import com.example.smartshop.data.model.review.Review
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    var productId = 0
    var reviewId = 0

    private var _reviewList: MutableStateFlow<ResultWrapper<out List<Review>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val reviewList: StateFlow<ResultWrapper<out List<Review>>> =
        _reviewList

    private var _removeReview: MutableStateFlow<ResultWrapper<out RemoveReviewResponse>> =
        MutableStateFlow(ResultWrapper.Loading)
    val removeReview: StateFlow<ResultWrapper<out RemoveReviewResponse>> =
        _removeReview


    fun getProductReviewList() {
        launch {
            repository.getProductReviewList(productId).collect {
                _reviewList.emit(it)
            }
        }
    }

    fun removeReview() {
        launch {
            repository.removeReview(reviewId).collect{
                _removeReview.emit(it)
            }
        }
    }

}