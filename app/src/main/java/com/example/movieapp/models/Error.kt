package com.example.movieapp.models

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    val success: Boolean
)
