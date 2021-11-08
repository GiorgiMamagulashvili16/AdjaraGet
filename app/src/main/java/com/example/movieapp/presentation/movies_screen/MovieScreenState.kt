package com.example.movieapp.presentation.movies_screen

import com.example.movieapp.models.Movie

data class MovieScreenState(
    val isLoading: Boolean = false,
    val data: List<Movie>? = null,
    val error: String? = null
)