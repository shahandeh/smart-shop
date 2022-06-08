package com.example.smartshop.data

import com.example.smartshop.BuildConfig
import com.example.smartshop.data.model.customer.CreateCustomer
import com.example.smartshop.data.model.customer.RetrieveCustomer
import com.example.smartshop.data.model.order.CreateOrder
import com.example.smartshop.data.model.order.CreateOrderResponse
import com.example.smartshop.data.model.order.GetOrder
import com.example.smartshop.data.model.order.UpdateOrder
import com.example.smartshop.data.model.product.Category
import com.example.smartshop.data.model.product.Product
import com.example.smartshop.data.model.review.CreateReview
import com.example.smartshop.data.model.review.CreateReviewResponse
import com.example.smartshop.data.model.review.RemoveReviewResponse
import com.example.smartshop.data.model.review.Review
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface IRemoteDataSource {

    suspend fun getProductListByOrder(page: Int, orderBy: String): Response<List<Product>>

    suspend fun getProductListByCategory(page: Int, category: String): Response<List<Product>>

    suspend fun getCategoryList(): Response<List<Category>>

    suspend fun getProduct(id: String): Response<Product>

    suspend fun searchProduct(param: String): Response<List<Product>>

    suspend fun createOrder(
        customerId: Int,
        createOrder: CreateOrder,
    ): Response<List<CreateOrderResponse>>

    suspend fun addToOrder(id: Int, createOrder: CreateOrder): Response<UpdateOrder>

    suspend fun updateOrder(id: Int, updateOrder: UpdateOrder): Response<UpdateOrder>

    suspend fun getOrderList(
        page: Int,
        status: String,
        customerId: Int,
        perPage: Int,
    ): Response<List<GetOrder>>

    suspend fun getOrderListByInclude(include: String): Response<List<Product>>

    suspend fun createUser(createCustomer: CreateCustomer): Response<RetrieveCustomer>

    suspend fun retrieveUser(id: String): Response<RetrieveCustomer>

    suspend fun retrieveUserList(userName: String): Response<List<RetrieveCustomer>>

    suspend fun getProductReviewList(productId: Int): Response<List<Review>>

    suspend fun createReview(createReview: CreateReview): Response<CreateReviewResponse>

    suspend fun removeReview(reviewId: Int): Response<RemoveReviewResponse>

    suspend fun updateReview(
        reviewId: Int,
        rating: Int,
        review: String,
        reviewer: String,
    ): Response<CreateReviewResponse>

}