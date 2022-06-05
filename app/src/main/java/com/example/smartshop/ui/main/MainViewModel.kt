package com.example.smartshop.ui.main

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.customer.RetrieveCustomer
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.tracker.NetworkStatus
import com.example.smartshop.tracker.NetworkStatusTracker
import com.example.smartshop.tracker.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShopRepository,
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

    val status = networkStatusTracker.networkStatus.map(
        onAvailable = { NetworkStatus.Available },
        onUnavailable = { NetworkStatus.Unavailable }
    )

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