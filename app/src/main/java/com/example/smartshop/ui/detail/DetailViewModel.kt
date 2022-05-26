package com.example.smartshop.ui.detail

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
class DetailViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private var _getProduct: MutableStateFlow<ResultWrapper<out Product?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct: StateFlow<ResultWrapper<out Product?>> =
        _getProduct

    fun getProduct(id: String) {
        viewModelScope.launch {
            repository.getProduct(id).collect {
                _getProduct.emit(it)
            }
        }
    }
}