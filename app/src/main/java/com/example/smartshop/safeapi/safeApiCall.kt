package com.example.smartshop.safeapi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import retrofit2.Response

fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>,
) = channelFlow<ResultWrapper<T>> {
    withContext(dispatcher) {
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null)
                send(ResultWrapper.Success(body))
            else if (response.code() == 400)
                send(ResultWrapper.Failure("Request Invalid"))
            else if (response.code() == 401)
                send(ResultWrapper.Failure("Unauthorized"))
            else if (response.code() == 404)
                send(ResultWrapper.Failure("Not Found"))
            else if (response.code() == 500)
                send(ResultWrapper.Failure("Internal Server Error"))
            else
                send(ResultWrapper.Failure("Unknown Error"))
        } catch (e: Exception) {
            send(ResultWrapper.Failure("Unknown Error"))
        }
    }
}