package com.example.smartshop.ui.detail

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.order.*
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.data.model.review.Review
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.reviewlist.ReviewDataModel
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    lateinit var product: Product
    lateinit var order: GetOrder
    var orderIsEmpty = false
    var orderContainProduct = false
    var productId = ""
    lateinit var reviewList: List<Review>

    private var _getProduct: MutableStateFlow<ResultWrapper<out Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct: StateFlow<ResultWrapper<out Product>> =
        _getProduct

    private var _getOrder: MutableStateFlow<ResultWrapper<out List<GetOrder>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getOrder: StateFlow<ResultWrapper<out List<GetOrder>>> =
        _getOrder

    private var _createOrder: MutableStateFlow<ResultWrapper<out List<CreateOrderResponse>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val createOrder: StateFlow<ResultWrapper<out List<CreateOrderResponse>>> =
        _createOrder

    private var _updateOrderResponse: MutableStateFlow<ResultWrapper<out UpdateOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val updateOrderResponse: StateFlow<ResultWrapper<out UpdateOrder>> =
        _updateOrderResponse

    private var _productReviewList: MutableStateFlow<ResultWrapper<out List<Review>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productReviewList: StateFlow<ResultWrapper<out List<Review>>> =
        _productReviewList

    init {
        getOrder()
    }

    fun getProduct() {
        launch {
            repository.getProduct(productId).collect {
                _getProduct.emit(it)
            }
        }
    }

    fun getOrder() {
        launch {
            repository.getOrderList(1, "pending", user_id, 1).collect {
                _getOrder.emit(it)
            }
        }
    }

    fun getProductReviewList() {
        launch {
            repository.getProductReviewList(productId.toInt()).collect {
                _productReviewList.emit(it)
            }
        }
    }

    fun createOrder() {
        val createLineItem = mutableListOf<CreateOrderLineItem>()
        createLineItem.add(
            CreateOrderLineItem(
                product.id,
                1
            )
        )
        val createOrder = CreateOrder(
            createLineItem
        )

        launch {
            repository.createOrder(user_id, createOrder).collect {
                _createOrder.emit(it)
            }
        }
    }

    fun addToOrder() {
        val createLineItem = mutableListOf<CreateOrderLineItem>()
        createLineItem.add(
            CreateOrderLineItem(
                product.id,
                1
            )
        )
        val updateOrder = CreateOrder(
            createLineItem
        )
        launch {
            repository.addToOrder(order.id, updateOrder).collect {
                _updateOrderResponse.emit(it)
            }
        }
    }

    fun createReviewDataList(): List<ReviewDataModel> {
        val temp = mutableListOf<ReviewDataModel>()
        if (reviewList.size < 3) {
            for (i in reviewList) {
                temp.add(
                    ReviewDataModel.Data(
                        i.date_created,
                        i.product_id,
                        i.review,
                        i.reviewer
                    )
                )
            }
        } else {
            for (i in 0 until 3) {
                temp.add(
                    ReviewDataModel.Data(
                        reviewList[i].date_created,
                        reviewList[i].product_id,
                        reviewList[i].review,
                        reviewList[i].reviewer
                    )
                )
            }
            temp.add(
                ReviewDataModel.Footer(
                    reviewList[0].product_id
                )
            )
        }
        return temp
    }

}