package com.example.smartshop.safeapi

sealed class ResultWrapper<T> {
    data class Success<T>(val value: T): ResultWrapper<T>()
    data class Failure<T>(val message: String?): ResultWrapper<T>()
    object Loading: ResultWrapper<Nothing>()
}