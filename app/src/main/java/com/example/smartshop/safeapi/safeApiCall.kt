package com.example.smartshop.safeapi

import kotlinx.coroutines.flow.flow
import retrofit2.Response

suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response = apiCall()
        val body = response.body()
        if (response.isSuccessful && response.body() != null) {
            emit(ResultWrapper.Success(body))
        }
        else if (response.code() == 400)
            emit(ResultWrapper.Failure("Request Invalid"))
        else if (response.code() == 401)
            emit(ResultWrapper.Failure("Unauthorized"))
        else if (response.code() == 404)
            emit(ResultWrapper.Failure("Not Found"))
        else if (response.code() == 500)
            emit(ResultWrapper.Failure("Internal Server Error"))
    } catch (e: Exception){
        emit(ResultWrapper.Failure("Unknown Error"))
    }
}