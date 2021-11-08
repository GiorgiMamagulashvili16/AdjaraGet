package com.example.movieapp.util.exceptions

import java.io.IOException

class InvalidApiKeyException:IOException() {
    override val message: String
        get() = "Invalid Api Key"
}