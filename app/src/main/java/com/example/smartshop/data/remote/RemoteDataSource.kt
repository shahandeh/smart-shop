package com.example.smartshop.data.remote

import com.example.smartshop.data.remote.service.IShopApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val iShopApi: IShopApi,
) {

    suspend fun getProductListByOrder(page: Int, orderBy: String) = iShopApi.getProductListByOrder(page, orderBy)

    suspend fun getProductListByCategory(page: Int, category: String) = iShopApi.getProductListByCategory(page, category)

    suspend fun getCategoryList() = iShopApi.getCategoryList()

    suspend fun getProduct(id: String) = iShopApi.getProduct(id)

    suspend fun searchProduct(param: String) = iShopApi.searchProduct(param)

    suspend fun getOrderList(pageNumber: Int) = iShopApi.getOrderList(pageNumber)

}