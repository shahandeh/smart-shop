package com.example.smartshop.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.Category
import com.example.smartshop.safeapi.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: ShopRepository
): ViewModel() {

    private var _getCategoryList: MutableStateFlow<ResultWrapper<out List<Category>?>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getCategoryList: StateFlow<ResultWrapper<out List<Category>?>> =
        _getCategoryList

    fun getCategoryList(){
        viewModelScope.launch {
            repository.getCategoryList(IO).collect {
                _getCategoryList.emit(it)
            }
        }
    }
}