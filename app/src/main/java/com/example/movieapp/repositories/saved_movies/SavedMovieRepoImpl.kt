package com.example.movieapp.repositories.saved_movies

import com.example.movieapp.db.MoviesDao
import com.example.movieapp.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedMovieRepoImpl @Inject constructor(
    private val dao: MoviesDao
) : SavedMoviesRepository {
    override suspend fun insertMovie(movie: Movie) = withContext(Dispatchers.IO) {
        dao.insertMovie(movie)
    }

    override suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        dao.getMovies()
    }
}