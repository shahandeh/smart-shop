package com.example.smartshop.ui.createreview

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.CurrentUser.email
import com.example.smartshop.data.CurrentUser.user_name
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.review.CreateReview
import com.example.smartshop.data.model.review.CreateReviewResponse
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    var productId = ""
    var reviewId = ""
    var suggestTitle = ""
    var suggestMessage = ""
    var positive = ""
    var negative = ""
    var body = ""
    var point = 0
    var userName = ""

    private var _createReviewResponse: MutableStateFlow<ResultWrapper<out CreateReviewResponse>> =
        MutableStateFlow(ResultWrapper.Loading)
    val createReviewResponse: StateFlow<ResultWrapper<out CreateReviewResponse>> =
        _createReviewResponse

    fun createReview() {
            if (productId.isNotBlank() && productId != "null") {
                launch {
                    repository.createReview(
                        CreateReview(
                            productId.toInt(),
                            suggestTitle + "\n" + suggestMessage + "\n" + positive + "\n" + negative + "\n" + body,
                            userName,
                            email,
                            point
                        )
                    ).collect {
                        _createReviewResponse.emit(it)
                    }
                    productId = ""
                }
            } else {
                launch {
                    repository.updateReview(
                        reviewId.toInt(),
                        point,
                        suggestTitle + "\n" + suggestMessage + "\n" + positive + "\n" + negative + "\n" + body,
                        userName
                    ).collect {
                        _createReviewResponse.emit(it)
                    }
                    reviewId = ""
                }
            }
    }

}