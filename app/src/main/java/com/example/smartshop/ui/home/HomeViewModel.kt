package com.example.smartshop.ui.home

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ShopRepository): ViewModel() {

    suspend fun getProductList(page: Int, orderBy: String) = repository.getProductList(page, orderBy)

}