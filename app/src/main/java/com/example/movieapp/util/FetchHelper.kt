package com.example.movieapp.util

import com.example.movieapp.models.Error
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

inline fun <T> fetchData(call: () -> Response<T>): ResponseHandler<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            ResponseHandler.Success(response.body()!!)
        } else {
            ResponseHandler.Error(response.errorBody()?.parseErrorBody() ?: "unKnown error")
        }
    } catch (e: IOException) {
        ResponseHandler.Error(e.localizedMessage!!)
    } catch (e: HttpException) {
        ResponseHandler.Error(e.localizedMessage!!)
    }
}

fun ResponseBody.parseErrorBody(): String {
    return Gson().fromJson(this.string(), Error::class.java).statusMessage
}