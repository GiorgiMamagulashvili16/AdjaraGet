package com.example.movieapp.util.exceptions

import com.example.movieapp.util.Constants.INVALID_API_KEY_MESSAGE
import java.io.IOException

class InvalidApiKeyException:IOException() {
    override val message: String
        get() = INVALID_API_KEY_MESSAGE
}