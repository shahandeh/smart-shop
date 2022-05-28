package com.example.smartshop.data

import com.example.smartshop.data.remote.RemoteDataSource
import com.example.smartshop.safeapi.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getProductListByOrder(page: Int, orderBy: String) =
        safeApiCall { remoteDataSource.getProductListByOrder(page, orderBy) }

    suspend fun getProductListByCategory(page: Int, category: String) =
        safeApiCall { remoteDataSource.getProductListByCategory(page, category) }

    suspend fun getCategoryList() = safeApiCall { remoteDataSource.getCategoryList() }

    suspend fun getProduct(id: String) = safeApiCall { remoteDataSource.getProduct(id) }

    suspend fun searchProduct(param: String) = safeApiCall { remoteDataSource.searchProduct(param) }
}