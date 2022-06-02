package com.example.smartshop.di

import com.example.smartshop.constant.BASE_URL
import com.example.smartshop.data.remote.service.IShopApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShopNetworkModule {

    @Singleton
    @Provides
    fun provideGson(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gson)
        .build()

    @Singleton
    @Provides
    fun provideService(
        retrofit: Retrofit,
    ): IShopApi = retrofit.create(IShopApi::class.java)

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val service = retrofit.create(IShopApi::class.java)
}