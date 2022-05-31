package com.example.smartshop.ui.home

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
class HomeViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    private var _getProductListByDate: MutableStateFlow<ResultWrapper<out List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByDate: StateFlow<ResultWrapper<out List<Product>>> = _getProductListByDate

    private var _getProductListByPopularity: MutableStateFlow<ResultWrapper<out List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByPopularity: StateFlow<ResultWrapper<out List<Product>>> =
        _getProductListByPopularity

    private var _getProductListByRating: MutableStateFlow<ResultWrapper<out List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByRating: StateFlow<ResultWrapper<out List<Product>>> =
        _getProductListByRating

    fun getProductListByDate() {
        launch {
            repository.getProductListByOrder(1, "date", IO).collect {
                _getProductListByDate.emit(it)
            }
        }
    }

    fun getProductListByPopularity() {
        launch {
            repository.getProductListByOrder(1, "popularity", IO).collect {
                _getProductListByPopularity.emit(it)
            }
        }
    }

    fun getProductListByRating() {
        launch {
            repository.getProductListByOrder(1, "rating", IO).collect {
                _getProductListByRating.emit(it)
            }
        }
    }
}