package com.example.movieapp.network.interceptors

import com.example.movieapp.util.exceptions.InvalidApiKeyException
import com.example.movieapp.util.exceptions.NotFoundException
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {

            when (response.code) {
                401 -> throw InvalidApiKeyException()
                404 -> throw NotFoundException()
            }
        }
        return response
    }
}