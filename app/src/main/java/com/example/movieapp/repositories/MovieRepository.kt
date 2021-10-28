package com.example.movieapp.repositories

import com.example.movieapp.models.Genre
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.ResponseHandler

interface MovieRepository {
    suspend fun getMovieById(movieId: Int): ResponseHandler<Movie>
    suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse>
    suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse>
}