package com.example.smartshop.worker

import com.example.smartshop.BuildConfig
import com.example.smartshop.data.model.product.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WorkerApi {

    @GET("wp-json/wc/v3/products")
    suspend fun getNewProductList(
        @Query("after") after: String,
        @Query("consumer_key") consumer_key: String = BuildConfig.CONSUMER_KEY,
        @Query("consumer_secret") consumer_secret: String = BuildConfig.CONSUMER_SECRET,
    ): Response<List<Product>>
}