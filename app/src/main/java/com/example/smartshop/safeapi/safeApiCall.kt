package com.example.smartshop.safeapi

import com.example.smartshop.data.model.ErrorBody
import com.example.smartshop.util.cleaner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>,
) = channelFlow<ResultWrapper<out T>> {
    withContext(dispatcher) {
        send(ResultWrapper.Loading)
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null)
                send(ResultWrapper.Success(body))
            else if (response.errorBody() != null)
                send(ResultWrapper.Failure(errorBody(response.errorBody())))
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

private fun errorBody(errorBody: ResponseBody?): String {
    val gson = Gson()
    val type = object : TypeToken<ErrorBody>() {}.type
    return gson.fromJson<ErrorBody?>(errorBody!!.charStream(), type).code.cleaner()
}