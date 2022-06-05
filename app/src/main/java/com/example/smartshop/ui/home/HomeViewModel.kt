package com.example.smartshop.ui.home

import androidx.lifecycle.ViewModel
import com.example.smartshop.data.ShopRepository
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.safeapi.ResultWrapper
import com.example.smartshop.ui.adapter.DataModel
import com.example.smartshop.util.launch
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var _getImageSlider: MutableStateFlow<ResultWrapper<out Product>> =
        MutableStateFlow(ResultWrapper.Loading)
    val getImageSlider: StateFlow<ResultWrapper<out Product>> =
        _getImageSlider

    fun getProductListByDate() {
        launch {
            repository.getProductListByOrder(1, "date").collect {
                _getProductListByDate.emit(it)
            }
        }
    }

    fun getProductListByPopularity() {
        launch {
            repository.getProductListByOrder(1, "popularity").collect {
                _getProductListByPopularity.emit(it)
            }
        }
    }

    fun getProductListByRating() {
        launch {
            repository.getProductListByOrder(1, "rating").collect {
                _getProductListByRating.emit(it)
            }
        }
    }

    fun getImageSliderProduct() {
        launch {
            repository.getProduct("608").collect {
                _getImageSlider.emit(it)
            }
        }
    }

    fun productList(list: List<Product>, title: String, order: String): List<DataModel> {
        val temp = mutableListOf<DataModel>()
        temp.add(
            DataModel.Header(
                title = title,
                order = order
            )
        )

        for (i in list) {
            temp.add(
                DataModel.Data(
                    i
                )
            )
        }

        temp.add(
            DataModel.Footer(
                order = order
            )
        )
        return temp
    }

    fun createDataList(list: List<Product>): List<DataModel> {
        return productList(
            list,
            "جدیدترین محصولات",
            "date"
        )
    }

    fun createPopularityList(list: List<Product>): List<DataModel> {
        return productList(
            list,
            "پر بازدیدترین محصولات",
            "popularity"
        )
    }

    fun createRatedList(list: List<Product>): List<DataModel> {
        return productList(
            list,
            "بهترین محصولات",
            "rating"
        )
    }
}