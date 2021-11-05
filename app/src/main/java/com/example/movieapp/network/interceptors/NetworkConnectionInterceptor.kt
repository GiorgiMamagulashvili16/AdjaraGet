package com.example.movieapp.network.interceptors

import com.example.movieapp.util.Constants.COMMAND_FOR_PING
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
            val command = COMMAND_FOR_PING
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }
}