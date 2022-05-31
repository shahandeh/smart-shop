package com.example.smartshop.ui.orderhistory

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.Order
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: ShopRepository
): ViewModel() {

    var cachedList = mutableListOf<Order>()
    var isLoading = false
    var pageNumber = 1

    private var _orderHistory: MutableStateFlow<ResultWrapper<out List<Order>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val orderHistory: StateFlow<ResultWrapper<out List<Order>>> =
        _orderHistory

    fun getOrderHistory(pageNumber: Int) {
        launch {
            repository.getOrderList(IO, pageNumber).collect {
                _orderHistory.emit(it)
            }
        }
    }
}