package com.example.smartshop.data.remote.service

import com.example.smartshop.BuildConfig
import com.example.smartshop.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IShopApi {

    @GET("wp-json/wc/v3/products")
    suspend fun getProductList(
        @Query ("page") page: Int,
        @Query ("orderby") orderBy: String,
        @Query ("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query ("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>

}