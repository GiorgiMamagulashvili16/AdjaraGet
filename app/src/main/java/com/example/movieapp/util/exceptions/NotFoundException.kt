package com.example.movieapp.util.exceptions

import com.example.movieapp.util.Constants.NOT_FOUND_MESSAGE
import java.io.IOException

class NotFoundException : IOException() {
    override val message: String
        get() = NOT_FOUND_MESSAGE
}