package com.example.movieapp.network

import com.example.movieapp.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/3/movie/top_rated?")
    suspend fun getTopRatedMovies(
        @Query("page")
        page: Int
    ): Response<MovieResponse>

    @GET("/3/movie/popular?")
    suspend fun getPopularMovies(
        @Query("page")
        page: Int
    ): Response<MovieResponse>
}