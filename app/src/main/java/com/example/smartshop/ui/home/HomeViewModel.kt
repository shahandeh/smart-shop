package com.example.smartshop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.Product
import com.example.smartshop.safeapi.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private var _getProductListByDate: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByDate: StateFlow<ResultWrapper<out List<Product>?>> = _getProductListByDate

    private var _getProductListByPopularity: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByPopularity: StateFlow<ResultWrapper<out List<Product>?>> =
        _getProductListByPopularity

    private var _getProductListByRating: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProductListByRating: StateFlow<ResultWrapper<out List<Product>?>> =
        _getProductListByRating

    fun getProductListByDate() {
        viewModelScope.launch {
            repository.getProductListByOrder(1, "date").collect {
                _getProductListByDate.emit(it)

            }
        }
    }

    fun getProductListByPopularity() {
        viewModelScope.launch {
            repository.getProductListByOrder(1, "popularity").collect {
                _getProductListByPopularity.emit(it)
            }
        }
    }

    fun getProductListByRating() {
        viewModelScope.launch {
            repository.getProductListByOrder(1, "rating").collect {
                _getProductListByRating.emit(it)

            }
        }
    }


}