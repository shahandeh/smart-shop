package com.example.smartshop.ui.order

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.smartshop.data.CurrentUser
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.createOrderResponse.LineItem
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.data.model.order.UpdateLineItem
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.data.model.product.ProductInOrder
import com.example.smartshop.data.model.test.TEST
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    lateinit var currentOrder: TEST
    lateinit var currentResponse: UpdateOrder
    var listUpdated = false
    var productList = mutableListOf<Product>()
//    lateinit var updateResponseList: List<UpdateLineItem>

    private var _order: MutableStateFlow<ResultWrapper<out List<TEST>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val order: StateFlow<ResultWrapper<out List<TEST>>> =
        _order

    private var _getProduct: MutableStateFlow<ResultWrapper<out Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct: StateFlow<ResultWrapper<out Product>> =
        _getProduct

    private var _updateOrderResponse: MutableStateFlow<ResultWrapper<out UpdateOrder>> =
        MutableStateFlow(ResultWrapper.Loading)
    val updateOrderResponse: StateFlow<ResultWrapper<out UpdateOrder>> =
        _updateOrderResponse


    fun getOrder() {
        launch {
            repository.getOrderList(IO, 1, "pending", user_id, 1).collect {
                _order.emit(it)
            }
        }
    }

    fun getProductInOrder() {
        Log.d(TAG, "onViewCreated: order")
        launch {
            for (i in currentOrder.line_items) {
                repository.getProduct(i.product_id.toString(), IO).collect {
                    _getProduct.emit(it)
                }

            }
        }
    }

    fun createProductListFromOrder(): List<ProductInOrder> {
        val temp = mutableListOf<ProductInOrder>()
        for (i in currentOrder.line_items) {
            for (j in productList) {
                if (i.product_id == j.id) {
                    temp.add(
                        ProductInOrder(
                            j.id,
                            j.name,
                            j.description,
                            j.images[0].src,
                            j.sale_price,
                            j.regular_price,
                            i.quantity
                        )
                    )
                }
            }
        }

        productList.clear()
        return temp
    }

    fun getProductInResponse() {
        Log.d(TAG, "onViewCreated: response")
        launch {
            for (i in currentResponse.line_items) {
                repository.getProduct(i.product_id.toString(), IO).collect {
                    _getProduct.emit(it)
                }
            }
        }
    }

    fun createProductListFromResponse(): List<ProductInOrder> {
        val temp = mutableListOf<ProductInOrder>()
        for (i in currentResponse.line_items) {
            for (j in productList) {
                if (i.id == j.id) {
                    temp.add(
                        ProductInOrder(
                            j.id,
                            j.name,
                            j.description,
                            j.images[0].src,
                            j.sale_price,
                            j.regular_price,
                            i.quantity
                        )
                    )
                }
            }
        }
        return temp
    }

    fun productCount(id: Int, isPlus: Boolean) {
        val temp = mutableListOf<UpdateLineItem>()
        for (i in currentOrder.line_items) {
            if (i.product_id == id && isPlus) {
                temp.add(
                    UpdateLineItem(
                        i.id,
                        i.product_id,
                        i.quantity + 1
                    )
                )
            } else if (i.product_id == id && !isPlus) {
                temp.add(
                    UpdateLineItem(
                        i.id,
                        i.product_id,
                        i.quantity - 1
                    )
                )
            } else {
                temp.add(
                    UpdateLineItem(
                        i.id,
                        i.product_id,
                        i.quantity
                    )
                )
            }
        }
        launch {
            repository.updateOrder(
                IO,
                currentOrder.id,
                UpdateOrder(temp)
            ).collect {
                _updateOrderResponse.emit(it)
            }
        }
    }

    fun orderComplete() {
        val temp = mutableListOf<UpdateLineItem>()
        for (i in currentOrder.line_items) {
            temp.add(
                UpdateLineItem(
                    i.id,
                    i.product_id,
                    i.quantity
                )
            )
        }
        launch {
            repository.updateOrder(
                IO,
                currentOrder.id,
                UpdateOrder(
                    temp,
                    "completed"
                )
            ).collect {
                _updateOrderResponse.emit(it)
            }
        }
    }

}