package com.example.movieapp.repositories.saved_movies

import com.example.movieapp.models.Movie
import kotlinx.coroutines.flow.Flow

interface SavedMoviesRepository {

    suspend fun insertMovie(movie: Movie)
    suspend fun getMovies(): List<Movie>
}