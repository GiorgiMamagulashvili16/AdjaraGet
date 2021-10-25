package com.example.movieapp.repositories

import androidx.paging.PagingData
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.ResponseHandler
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieById(movieId: Int): ResponseHandler<MovieDetailResponse>
    suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse>
    suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse>
}