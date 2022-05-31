package com.example.smartshop.ui.productlist

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.Product
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    var cachedList = mutableListOf<Product>()
    var isLoading = false
    var pageNumber = 1

    private var _getProductListByOrder: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByOrder: StateFlow<ResultWrapper<out List<Product>?>> =
        _getProductListByOrder

    private var _getProductListByCategory: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByCategory: StateFlow<ResultWrapper<out List<Product>?>> =
        _getProductListByCategory

    fun getProductListByOrder(page: Int, orderBy: String) {
        launch {
            repository.getProductListByOrder(page, orderBy, IO).collect {
                _getProductListByOrder.emit(it)
            }
        }
    }

    fun getProductListByCategory(page: Int, category: String) {
        launch {
            repository.getProductListByCategory(page, category, IO).collect {
                _getProductListByCategory.emit(it)
            }
        }
    }

}