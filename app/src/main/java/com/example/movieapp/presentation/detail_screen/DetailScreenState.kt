package com.example.movieapp.presentation.detail_screen

import com.example.movieapp.models.Movie


data class DetailScreenState(
    val isLoading: Boolean = false,
    val data: Movie? = null,
    val error: String? = null
)
