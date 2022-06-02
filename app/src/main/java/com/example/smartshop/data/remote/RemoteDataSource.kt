package com.example.smartshop.data.remote

import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.order.CreateOrder
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.remote.service.IShopApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val iShopApi: IShopApi,
) {

    suspend fun getProductListByOrder(page: Int, orderBy: String) =
        iShopApi.getProductListByOrder(page, orderBy)

    suspend fun getProductListByCategory(page: Int, category: String) =
        iShopApi.getProductListByCategory(page, category)

    suspend fun getCategoryList() = iShopApi.getCategoryList()

    suspend fun getProduct(id: String) = iShopApi.getProduct(id)

    suspend fun searchProduct(param: String) = iShopApi.searchProduct(param)

    suspend fun createOrder(customerId: Int, createOrder: CreateOrder) = iShopApi.createOrder(customerId, createOrder)

    suspend fun addToOrder(id: Int, createOrder: CreateOrder) = iShopApi.addToOrder(id, createOrder)

    suspend fun updateOrder(id: Int, updateOrder: UpdateOrder) = iShopApi.updateOrder(id, updateOrder)

    suspend fun getOrderList(page: Int, status: String, customerId: Int, perPage: Int) =
        iShopApi.getOrderList(page, status, customerId, perPage)

    suspend fun createUser(createCustomer: CreateCustomer) = iShopApi.createUser(createCustomer)

    suspend fun retrieveUser(id: String) = iShopApi.retrieveUser(id)

    suspend fun retrieveUserList(userName: String) = iShopApi.retrieveUserList(userName)

}