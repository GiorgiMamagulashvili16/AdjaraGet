package com.example.movieapp.network

import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("/3/movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovies(
        @Query("page")
        page: Int
    ): Response<MovieResponse>

    @GET("/3/movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(
        @Query("page")
        page: Int
    ): Response<MovieResponse>

    @GET("/3/movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieById(
        @Path("movie_id")
        movieId: Int
    ): Response<Movie>
}