package com.example.movieapp.repositories

import android.util.Log.d
import com.example.movieapp.models.Error
import com.example.movieapp.models.Genre
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.network.MovieService
import com.example.movieapp.util.ResponseHandler
import com.example.movieapp.util.fetchData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository {

    override suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse> {
        return withContext(Dispatchers.IO) {
            fetchData {
                movieService.getTopRatedMovies(page)
            }
        }
    }

    override suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse> {
        return withContext(Dispatchers.IO) {
            fetchData {
                movieService.getPopularMovies(page)
            }
        }
    }
}
