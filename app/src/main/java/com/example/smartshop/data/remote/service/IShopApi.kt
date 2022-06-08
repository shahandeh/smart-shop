package com.example.smartshop.data.remote.service

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
import retrofit2.http.*

interface IShopApi {

    @GET("wp-json/wc/v3/products")
    suspend fun getProductListByOrder(
        @Query("page") page: Int,
        @Query("orderby") orderBy: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @GET("wp-json/wc/v3/products")
    suspend fun getProductListByCategory(
        @Query("page") page: Int,
        @Query("category") category: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @GET("wp-json/wc/v3/products/categories")
    suspend fun getCategoryList(
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Category>>

    @GET("wp-json/wc/v3/products/{id}")
    suspend fun getProduct(
        @Path("id") id: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<Product>

    @GET("wp-json/wc/v3/products")
    suspend fun searchProduct(
        @Query("search") search: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @POST("/wp-json/wc/v3/orders")
    suspend fun createOrder(
        @Query("customer_id") customerId: Int,
        @Body createOrder: CreateOrder,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<CreateOrderResponse>>

    @PUT("/wp-json/wc/v3/orders/{id}")
    suspend fun addToOrder(
        @Path("id") id: Int,
        @Body createOrder: CreateOrder,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<UpdateOrder>

    @PUT("/wp-json/wc/v3/orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body updateOrder: UpdateOrder,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<UpdateOrder>

    @GET("wp-json/wc/v3/orders")
    suspend fun getOrderList(
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("customer") customerId: Int,
        @Query("per_page") perPage: Int,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<GetOrder>>

    @GET("wp-json/wc/v3/products")
    suspend fun getOrderListByInclude(
        @Query("include") include: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @POST("wp-json/wc/v3/customers")
    suspend fun createUser(
        @Body createCustomer: CreateCustomer,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<RetrieveCustomer>

    @GET("wp-json/wc/v3/customers/{id}")
    suspend fun retrieveUser(
        @Path("id") id: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<RetrieveCustomer>

    @GET("wp-json/wc/v3/customers")
    suspend fun retrieveUserList(
        @Query("search") userName: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<RetrieveCustomer>>

    @GET("wp-json/wc/v3/products/reviews")
    suspend fun getProductReviewList(
        @Query("product") productId: Int,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Review>>

    @POST("wp-json/wc/v3/products/reviews")
    suspend fun createReview(
        @Body createReview: CreateReview,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<CreateReviewResponse>

    @DELETE("wp-json/wc/v3/products/reviews/{id}")
    suspend fun removeReview(
        @Path("id") reviewId: Int,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<RemoveReviewResponse>

    @PUT("wp-json/wc/v3/products/reviews/{id}")
    suspend fun updateReview(
        @Path("id") reviewId: Int,
        @Query ("rating") rating: Int,
        @Query ("review") review: String,
        @Query ("reviewer") reviewer: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<CreateReviewResponse>

}