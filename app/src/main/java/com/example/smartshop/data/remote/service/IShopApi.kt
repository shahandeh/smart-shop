package com.example.smartshop.data.remote.service

import com.example.smartshop.BuildConfig
import com.example.smartshop.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface IShopApi {

    @GET("wp-json/wc/v3/products")
    suspend fun getProductListByOrder(
        @Query ("page") page: Int,
        @Query ("orderby") orderBy: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @GET("wp-json/wc/v3/products")
    suspend fun getProductListByCategory(
        @Query ("page") page: Int,
        @Query ("category") category: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @GET("wp-json/wc/v3/products/categories")
    suspend fun getCategoryList(
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Category>>

    @GET("wp-json/wc/v3/products/{id}")
    suspend fun getProduct(
        @Path("id") id: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<Product>

    @GET("wp-json/wc/v3/products")
    suspend fun searchProduct(
        @Query("search") search: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

    @GET("wp-json/wc/v3/orders")
    suspend fun getOrderList(
        @Query ("page") page: Int,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET
    ): Response<List<Order>>

    @POST("wp-json/wc/v3/customers")
    suspend fun createUser(
        @Body createCustomer: CreateCustomer,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET
    ): Response<RetrieveCustomer>

    @GET("wp-json/wc/v3/customers/{id}")
    suspend fun retrieveUser(
        @Path("id") id: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET
    ): Response<RetrieveCustomer>

    @GET("wp-json/wc/v3/customers")
    suspend fun retrieveUserList(
        @Query("search") userName: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET
    ): Response<List<RetrieveCustomer>>

}