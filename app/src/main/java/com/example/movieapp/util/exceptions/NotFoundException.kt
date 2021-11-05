package com.example.movieapp.util.exceptions

import java.io.IOException

class NotFoundException : IOException() {
    override val message: String
        get() = "Movies Not Found"
}