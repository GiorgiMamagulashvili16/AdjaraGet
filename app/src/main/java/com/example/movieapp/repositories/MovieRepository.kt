package com.example.movieapp.repositories

import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.ResponseHandler

interface MovieRepository {
    suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse>
    suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse>
    suspend fun getMovieById(movieId: Int): ResponseHandler<MovieDetailResponse>
}