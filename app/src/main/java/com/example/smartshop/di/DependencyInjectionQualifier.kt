package com.example.smartshop.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IRemoteDataSourceDependencyInjection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher