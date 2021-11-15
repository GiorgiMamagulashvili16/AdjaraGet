package com.example.movieapp.network.interceptors

import com.example.movieapp.util.exceptions.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class NetworkConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected())
            throw NoConnectionException()
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun isConnected(): Boolean {
        return try {
            Runtime.getRuntime().exec(COMMAND).waitFor() == PING_TIME
        } catch (e: Exception) {
            false
        }
    }
    companion object{
        private const val COMMAND = "ping -c 1 google.com"
        private const val PING_TIME = 0
    }
}