package com.example.movieapp.repositories

import androidx.paging.PagingData
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.ResponseHandler
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
     fun getTopRatedMovies(): Flow<PagingData<Movie>>
     fun getPopularMovies(): Flow<PagingData<Movie>>
    suspend fun getMovieById(movieId: Int): ResponseHandler<MovieDetailResponse>
//    suspend fun getTopRated():ResponseHandler<List<Movie>>
//    suspend fun get
}