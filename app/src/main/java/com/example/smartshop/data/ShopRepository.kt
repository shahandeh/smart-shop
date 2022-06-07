package com.example.smartshop.data

import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.order.CreateOrder
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.di.IRemoteDataSourceDependencyInjection
import com.example.smartshop.di.IoDispatcher
import com.example.smartshop.safeapi.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    @IRemoteDataSourceDependencyInjection private val remoteDataSource: IRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {
    fun getProductListByOrder(page: Int, orderBy: String) =
        safeApiCall(dispatcher) { remoteDataSource.getProductListByOrder(page, orderBy) }

    fun getProductListByCategory(
        page: Int,
        category: String,
    ) =
        safeApiCall(dispatcher) { remoteDataSource.getProductListByCategory(page, category) }

    fun getCategoryList() =
        safeApiCall(dispatcher) { remoteDataSource.getCategoryList() }

    fun getProduct(id: String) =
        safeApiCall(dispatcher) { remoteDataSource.getProduct(id) }

    fun searchProduct(param: String) =
        safeApiCall(dispatcher) { remoteDataSource.searchProduct(param) }

    suspend fun createOrder(
        customerId: Int,
        createOrder: CreateOrder,
    ) =
        safeApiCall(dispatcher) { remoteDataSource.createOrder(customerId, createOrder) }

    suspend fun addToOrder(id: Int, createOrder: CreateOrder) =
        safeApiCall(dispatcher) { remoteDataSource.addToOrder(id, createOrder) }

    suspend fun updateOrder(id: Int, updateOrder: UpdateOrder) =
        safeApiCall(dispatcher) { remoteDataSource.updateOrder(id, updateOrder) }

    fun getOrderList(
        page: Int,
        status: String,
        customerId: Int,
        perPage: Int,
    ) = safeApiCall(dispatcher) { remoteDataSource.getOrderList(page, status, customerId, perPage) }

    suspend fun getOrderListByInclude(include: String) =
        safeApiCall(dispatcher) { remoteDataSource.getOrderListByInclude(include) }

    fun createUser(createCustomer: CreateCustomer) =
        safeApiCall(dispatcher) { remoteDataSource.createUser(createCustomer) }

    fun retrieveUser(id: String) =
        safeApiCall(dispatcher) { remoteDataSource.retrieveUser(id) }

    fun retrieveUserList(userName: String) =
        safeApiCall(dispatcher) { remoteDataSource.retrieveUserList(userName) }

    fun getProductReviewList(productId: Int) =
        safeApiCall(dispatcher) { remoteDataSource.getProductReviewList(productId) }

}