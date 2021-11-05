package com.example.movieapp.util.exceptions

import java.io.IOException

class NoConnectionException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}