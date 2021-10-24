package com.example.movieapp.util

import java.net.InetSocketAddress
import java.net.Socket

object ConnectionChecker {

    fun hasInternet():Boolean{
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}