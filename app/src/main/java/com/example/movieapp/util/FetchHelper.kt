package com.example.movieapp.util

import retrofit2.HttpException
import java.io.IOException

inline fun <T> fetchData(fetch: () -> ResponseHandler<T>): ResponseHandler<T> {
    return try {
        fetch()
    } catch (e: IOException) {
        ResponseHandler.Error(e.localizedMessage!!)
    } catch (e: HttpException) {
        ResponseHandler.Error(e.localizedMessage!!)
    }
}