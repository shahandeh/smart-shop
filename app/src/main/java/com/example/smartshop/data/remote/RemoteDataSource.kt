package com.example.smartshop.data.remote

import com.example.smartshop.data.remote.service.IShopApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val iShopApi: IShopApi
) {

    suspend fun getProductList(page: Int, orderBy: String) = iShopApi.getProductList(page, orderBy)

}