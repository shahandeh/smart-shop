package com.example.smartshop.ui.orderdetail

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.CurrentUser
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.coupon.ValidateCouponResponse
import com.example.smartshop.data.model.order.Coupon
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.model.order.UpdateOrderResponse
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    lateinit var order: GetOrder
    lateinit var orderUpdate: UpdateOrderResponse
    lateinit var couponCode: String
    lateinit var orderId: String
    lateinit var price: String
    lateinit var discount: String
    lateinit var finalPrice: String
    var couponAmount = ""

    private var _currentOrder: MutableStateFlow<ResultWrapper<out List<GetOrder>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val currentOrder: StateFlow<ResultWrapper<out List<GetOrder>>> =
        _currentOrder

    private var _orderResponse: MutableStateFlow<ResultWrapper<out UpdateOrderResponse>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderResponse: StateFlow<ResultWrapper<out UpdateOrderResponse>> =
        _orderResponse

    private var _updateOrderCoupon: MutableStateFlow<ResultWrapper<out UpdateOrderResponse>> =
        MutableStateFlow(ResultWrapper.Loading)
    val updateOrderCoupon: StateFlow<ResultWrapper<out UpdateOrderResponse>> =
        _updateOrderCoupon

    private var _validateCouponResponse: MutableStateFlow<ResultWrapper<out List<ValidateCouponResponse>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val validateCouponResponse: StateFlow<ResultWrapper<out List<ValidateCouponResponse>>> =
        _validateCouponResponse

    fun setDateFromOrder() {
        orderId = order.id.toString()
        price = order.total
        discount = order.discount_total
        finalPrice = (order.total.toInt() - order.discount_total.toInt()).toString()
    }

    fun setDataFromUpdate() {
        price = orderUpdate.total
        discount = orderUpdate.discount_total
        finalPrice = (orderUpdate.total.toInt() - orderUpdate.discount_total.toInt()).toString()
    }

    fun getOrder() {
        launch {
            repository.getOrderList(1, "pending", CurrentUser.user_id, 1).collect {
                _currentOrder.emit(it)
            }
        }
    }

    fun updateOrder() {
        val temp = UpdateOrder(order.line_items, "completed")
        launch {
            repository.updateOrder(order.id, temp).collect {
                _orderResponse.emit(it)
            }
        }
    }

    fun validateCoupon() {
        launch {
            repository.validateCoupon(couponCode).collect {
                _validateCouponResponse.emit(it)
            }
        }
    }

    fun setCoupon() {
        launch {
            repository.setCoupon(order.id,
                UpdateOrder(coupon_lines = mutableListOf(Coupon(couponCode, couponAmount))))
                .collect {
                    _updateOrderCoupon.emit(it)
                }
        }
    }
}