package com.example.movieapp.presentation.detail_screen

import com.example.movieapp.models.MovieDetailResponse

data class DetailScreenState(
    val isLoading: Boolean = false,
    val data: MovieDetailResponse? = null,
    val error: String? = null
)
