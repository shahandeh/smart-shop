package com.example.smartshop.data.remote

import com.example.smartshop.data.IRemoteDataSource
import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.order.CreateOrder
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.model.review.CreateReview
import com.example.smartshop.data.model.review.RemoveReviewResponse
import com.example.smartshop.data.remote.service.IShopApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val iShopApi: IShopApi,
) : IRemoteDataSource {

    override suspend fun getProductListByOrder(page: Int, orderBy: String) =
        iShopApi.getProductListByOrder(page, orderBy)

    override suspend fun getProductListByCategory(page: Int, category: String) =
        iShopApi.getProductListByCategory(page, category)

    override suspend fun getCategoryList() = iShopApi.getCategoryList()

    override suspend fun getProduct(id: String) = iShopApi.getProduct(id)

    override suspend fun searchProduct(param: String) = iShopApi.searchProduct(param)

    override suspend fun createOrder(customerId: Int, createOrder: CreateOrder) =
        iShopApi.createOrder(customerId, createOrder)

    override suspend fun addToOrder(id: Int, createOrder: CreateOrder) =
        iShopApi.addToOrder(id, createOrder)

    override suspend fun updateOrder(id: Int, updateOrder: UpdateOrder) =
        iShopApi.updateOrder(id, updateOrder)

    override suspend fun getOrderList(page: Int, status: String, customerId: Int, perPage: Int) =
        iShopApi.getOrderList(page, status, customerId, perPage)

    override suspend fun getOrderListByInclude(include: String) =
        iShopApi.getOrderListByInclude(include)

    override suspend fun createUser(createCustomer: CreateCustomer) =
        iShopApi.createUser(createCustomer)

    override suspend fun retrieveUser(id: String) = iShopApi.retrieveUser(id)

    override suspend fun retrieveUserList(userName: String) = iShopApi.retrieveUserList(userName)

    override suspend fun getProductReviewList(productId: Int) =
        iShopApi.getProductReviewList(productId)

    override suspend fun createReview(createReview: CreateReview) =
        iShopApi.createReview(createReview)

    override suspend fun removeReview(reviewId: Int) = iShopApi.removeReview(reviewId)

    override suspend fun updateReview(
        reviewId: Int,
        rating: Int,
        review: String,
        reviewer: String,
    ) = iShopApi.updateReview(reviewId, rating, review, reviewer)

}