package com.example.smartshop.ui.order

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.CurrentUser.user_id
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.data.model.order.LineItem
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.model.order.UpdateOrderResponse
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.data.model.product.ProductInOrder
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private lateinit var updateOrder: UpdateOrder

    var productInOrderList = mutableListOf<ProductInOrder>()
    private var orderItemList = mutableListOf<LineItem>()
    private var orderId = 0

    private var _currentPendingOrder: MutableStateFlow<ResultWrapper<out List<GetOrder>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val currentPendingOrder: StateFlow<ResultWrapper<out List<GetOrder>>> =
        _currentPendingOrder

    private var _productList: MutableStateFlow<ResultWrapper<out List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productList: StateFlow<ResultWrapper<out List<Product>>> =
        _productList

    private var _updateOrderResponse: MutableStateFlow<ResultWrapper<out UpdateOrderResponse>> =
        MutableStateFlow(ResultWrapper.Loading)
    val updateOrderResponse: StateFlow<ResultWrapper<out UpdateOrderResponse>> =
        _updateOrderResponse

    fun getOrder() {
        launch {
            repository.getOrderList(
                1,
                "pending",
                user_id,
                1
            ).collect {
                _currentPendingOrder.emit(it)
            }
        }
    }

    fun setDataFromOrder(list: List<GetOrder>) {
        orderItemList = list[0].line_items as MutableList<LineItem>
        orderId = list[0].id
        createIdList()
    }

    private fun createIdList() {
        val idList = mutableListOf<Int>()
        idList.add(0)
        for (i in orderItemList) idList.add(i.product_id)
        getProductList(idList.toString())
    }

    private fun getProductList(list: String) {
        launch {
            repository.getOrderListByInclude(list).collect {
                _productList.emit(it)
            }
        }
    }

    fun createProductInOrderList(productList: List<Product>): List<ProductInOrder> {
        val temp = mutableListOf<ProductInOrder>()
        for (i in productList) temp.add(
            ProductInOrder(
                i.id,
                i.name,
                i.description,
                i.images[0].src,
                i.sale_price,
                i.regular_price,
                orderItemList.find { it.product_id == i.id }?.quantity ?: 0

            )
        )
        return temp
    }

    fun funQuantityPlus(productId: Int) {
        val temp = mutableListOf<LineItem>()
        for (i in orderItemList) {
            if (i.product_id == productId) {
                temp.add(
                    LineItem(
                        i.id,
                        i.product_id,
                        i.quantity + 1
                    )
                )
            } else temp.add(i)
        }
        createUpdateOrderModel(temp)
    }

    fun funQuantityMinus(productId: Int) {
        val temp = mutableListOf<LineItem>()
        for (i in orderItemList) {
            if (i.product_id == productId) {
                temp.add(
                    LineItem(
                        i.id,
                        i.product_id,
                        i.quantity - 1
                    )
                )
            } else temp.add(i)
        }
        createUpdateOrderModel(temp)
    }

    private fun createUpdateOrderModel(lineItemList: List<LineItem>) {
        updateOrder = UpdateOrder(lineItemList)
        updateOrder()
    }

    private fun updateOrder() {
        launch {
            repository.updateOrder(orderId, updateOrder).collect {
                _updateOrderResponse.emit(it)
            }
        }
    }

    fun setDataFromResponse(response: UpdateOrderResponse) {
        orderItemList = response.line_items as MutableList<LineItem>
        createIdList()
    }

    fun totalPrice(): String {
        var temp = 0
        for (i in productInOrderList) {
            temp += if (i.sale_price != "") {
                i.sale_price.toInt() * i.quantity
            } else {
                i.regular_price.toInt() * i.quantity
            }
        }
        return temp.toString()
    }

}