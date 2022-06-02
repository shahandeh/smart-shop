package com.example.smartshop.data

import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.order.CreateOrder
import com.example.smartshop.data.model.order.UpdateOrder
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

    suspend fun createOrder(dispatcher: CoroutineDispatcher, customerId: Int, createOrder: CreateOrder) =
        safeApiCall(dispatcher) { remoteDataSource.createOrder(customerId, createOrder) }

    suspend fun addToOrder(dispatcher: CoroutineDispatcher, id: Int, createOrder: CreateOrder) =
        safeApiCall(dispatcher) { remoteDataSource.addToOrder(id, createOrder) }

    suspend fun updateOrder(dispatcher: CoroutineDispatcher, id: Int, updateOrder: UpdateOrder) =
        safeApiCall(dispatcher) { remoteDataSource.updateOrder(id, updateOrder) }

    fun getOrderList(
        dispatcher: CoroutineDispatcher,
        page: Int,
        status: String,
        customerId: Int,
        perPage: Int,
    ) = safeApiCall(dispatcher) { remoteDataSource.getOrderList(page, status, customerId, perPage) }

    fun createUser(dispatcher: CoroutineDispatcher, createCustomer: CreateCustomer) =
        safeApiCall(dispatcher) { remoteDataSource.createUser(createCustomer) }

    fun retrieveUser(dispatcher: CoroutineDispatcher, id: String) =
        safeApiCall(dispatcher) { remoteDataSource.retrieveUser(id) }

    fun retrieveUserList(dispatcher: CoroutineDispatcher, userName: String) =
        safeApiCall(dispatcher) { remoteDataSource.retrieveUserList(userName) }

}