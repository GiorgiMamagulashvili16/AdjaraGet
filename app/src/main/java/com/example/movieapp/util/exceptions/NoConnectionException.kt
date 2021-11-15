package com.example.movieapp.util.exceptions

import com.example.movieapp.util.Constants.NO_INTERNET_MESSAGE
import java.io.IOException


class NoConnectionException: IOException() {
    override val message: String
        get() = NO_INTERNET_MESSAGE
}