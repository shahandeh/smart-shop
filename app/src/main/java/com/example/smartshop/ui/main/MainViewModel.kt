package com.example.smartshop.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.CreateCustomer
import com.example.smartshop.data.model.RetrieveCustomer
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private var _user: MutableStateFlow<ResultWrapper<out RetrieveCustomer>> =
        MutableStateFlow(ResultWrapper.Loading)
    val user: StateFlow<ResultWrapper<out RetrieveCustomer>> =
        _user

    suspend fun retrieveUser(id: String) {
        repository.retrieveUser(Dispatchers.IO, id).collect {
            _user.emit(it)
        }
    }

}