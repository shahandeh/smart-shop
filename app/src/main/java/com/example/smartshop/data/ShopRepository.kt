package com.example.smartshop.data

import com.example.smartshop.data.remote.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getProductList(page: Int, orderBy: String) = remoteDataSource.getProductList(page, orderBy)
}