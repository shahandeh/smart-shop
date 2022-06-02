package com.example.smartshop.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.createOrderResponse.CreateOrderResponse
import com.example.smartshop.data.model.order.*
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.data.model.test.TEST
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    lateinit var product: Product
    lateinit var order: TEST
    var orderIsEmpty = false
    var orderContainProduct = false

    private var _getProduct: MutableStateFlow<ResultWrapper<out Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct: StateFlow<ResultWrapper<out Product>> =
        _getProduct

    private var _getOrder: MutableStateFlow<ResultWrapper<out List<TEST>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getOrder: StateFlow<ResultWrapper<out List<TEST>>> =
        _getOrder

    private var _createOrder: MutableStateFlow<ResultWrapper<out List<CreateOrderResponse>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val createOrder: StateFlow<ResultWrapper<out List<CreateOrderResponse>>> =
        _createOrder

    private var _updateOrderResponse: MutableStateFlow<ResultWrapper<out UpdateOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val updateOrderResponse: StateFlow<ResultWrapper<out UpdateOrder>> =
        _updateOrderResponse

    init {
        Log.d("majid", "customerId DetailViewModer: $user_id")
        getOrder()
    }

    fun getProduct(id: String) {
        launch {
            repository.getProduct(id, IO).collect {
                _getProduct.emit(it)
            }
        }
    }

    fun getOrder() {
        launch {
            repository.getOrderList(IO, 1, "pending", user_id, 1).collect {
                _getOrder.emit(it)
            }
        }
    }

    fun createOrder() {
        val createLineItem = mutableListOf<CreateLineItem>()
        createLineItem.add(
            CreateLineItem(
                product.id,
                1
            )
        )
        val createOrder = CreateOrder(
            createLineItem
        )

        launch {
            repository.createOrder(IO, user_id, createOrder).collect{
                _createOrder.emit(it)
            }
        }
    }

    fun addToOrder() {
        val createLineItem = mutableListOf<CreateLineItem>()
        createLineItem.add(
            CreateLineItem(
                product.id,
                1
            )
        )
        val updateOrder = CreateOrder(
            createLineItem
        )
        launch {
            repository.addToOrder(IO, order.id, updateOrder).collect{
                _updateOrderResponse.emit(it)
            }
        }
    }

}