package com.example.movieapp.network.interceptors

import com.example.movieapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class QueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter(
            Constants.API_KEY_QUERY_PARAM,
            Constants.API_KEY
        )
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}