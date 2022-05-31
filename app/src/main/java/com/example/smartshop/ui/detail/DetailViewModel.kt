package com.example.smartshop.ui.detail

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
class DetailViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private var _getProduct: MutableStateFlow<ResultWrapper<out Product?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getProduct: StateFlow<ResultWrapper<out Product?>> =
        _getProduct

    fun getProduct(id: String) {
        launch {
            repository.getProduct(id, IO).collect {
                _getProduct.emit(it)
            }
        }
    }
}