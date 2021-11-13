package com.example.movieapp.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class QueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter(
           API_KEY_QUERY_PARAM, API_KEY
        )
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
    companion object{
        private const val API_KEY = "5722f4a67d3004aa2865e44355fff452"
        private const val API_KEY_QUERY_PARAM = "api_key"
    }
}