package com.example.movieapp.presentation.movies_screen

import androidx.paging.PagingData
import com.example.movieapp.models.Movie

data class MovieScreenState(
    val data: PagingData<Movie>? = null,
)