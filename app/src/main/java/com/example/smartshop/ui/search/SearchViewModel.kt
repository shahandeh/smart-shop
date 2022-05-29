package com.example.smartshop.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.Product
import com.example.smartshop.safeapi.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ShopRepository
): ViewModel() {

    var param = ""

    private var _searchResult: MutableStateFlow<ResultWrapper<out List<Product>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val searchResult: StateFlow<ResultWrapper<out List<Product>?>> =
        _searchResult

    fun searchProduct(param: String) {
        viewModelScope.launch {
            repository.searchProduct(param, IO).collect {
                _searchResult.emit(it)
            }
        }
    }
}