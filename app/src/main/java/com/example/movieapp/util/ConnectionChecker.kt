package com.example.movieapp.util

import java.net.InetSocketAddress
import java.net.Socket

object ConnectionChecker {

    fun hasInternet(): Boolean {
        return try {
            val command = "ping -c 1 google.com"
            (Runtime.getRuntime().exec(command).waitFor() == 0)
        } catch (e: Exception) {
            false
        }
    }
}