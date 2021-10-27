package com.example.movieapp.repositories

import com.example.movieapp.models.Movie

interface SavedMovieRepository {

    fun getAllMovies():List<Movie>
    suspend fun addMovie(movie: Movie)
}