package com.example.smartshop.data

import com.example.smartshop.data.model.CreateCustomer
import com.example.smartshop.data.remote.RemoteDataSource
import com.example.smartshop.safeapi.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    fun getProductListByOrder(page: Int, orderBy: String, dispatcher: CoroutineDispatcher) =
        safeApiCall(dispatcher) { remoteDataSource.getProductListByOrder(page, orderBy) }

    fun getProductListByCategory(
        page: Int,
        category: String,
        dispatcher: CoroutineDispatcher,
    ) =
        safeApiCall(dispatcher) { remoteDataSource.getProductListByCategory(page, category) }

    fun getCategoryList(dispatcher: CoroutineDispatcher) =
        safeApiCall(dispatcher) { remoteDataSource.getCategoryList() }

    fun getProduct(id: String, dispatcher: CoroutineDispatcher) =
        safeApiCall(dispatcher) { remoteDataSource.getProduct(id) }

    fun searchProduct(param: String, dispatcher: CoroutineDispatcher) =
        safeApiCall(dispatcher) { remoteDataSource.searchProduct(param) }

    fun getOrderList(dispatcher: CoroutineDispatcher, pageNumber: Int) =
        safeApiCall(dispatcher) { remoteDataSource.getOrderList(pageNumber) }

    fun createUser(dispatcher: CoroutineDispatcher, createCustomer: CreateCustomer) =
        safeApiCall(dispatcher) {remoteDataSource.createUser(createCustomer)}

    fun retrieveUser(dispatcher: CoroutineDispatcher, id: String) =
        safeApiCall(dispatcher) {remoteDataSource.retrieveUser(id)}

    fun retrieveUserList(dispatcher: CoroutineDispatcher, userName: String) =
        safeApiCall(dispatcher) {remoteDataSource.retrieveUserList(userName)}

}