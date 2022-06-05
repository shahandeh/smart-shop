package com.example.smartshop.di

import com.example.smartshop.constant.BASE_URL
import com.example.smartshop.data.IRemoteDataSource
import com.example.smartshop.data.remote.RemoteDataSource
import com.example.smartshop.data.remote.service.IShopApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShopModule {

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

    @Singleton
    @Provides
    @IRemoteDataSourceDependencyInjection
    fun provideRemoteDataSource(
        iShopApi: IShopApi
    ): IRemoteDataSource = RemoteDataSource(iShopApi)

    @Singleton
    @Provides
    @IoDispatcher
    fun provideDispatcher(): CoroutineDispatcher = IO

}