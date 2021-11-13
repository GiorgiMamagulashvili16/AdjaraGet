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
                INVALID_API_KEY_CODE -> throw InvalidApiKeyException()
                NOT_FOUND_CODE -> throw NotFoundException()
            }
        }
        return response
    }
    companion object{
        private const val INVALID_API_KEY_CODE = 401
        private const val NOT_FOUND_CODE = 404
    }
}