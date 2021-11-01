package com.example.movieapp.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    val results: MutableList<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

