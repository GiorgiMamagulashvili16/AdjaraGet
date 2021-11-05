package com.example.movieapp.repositories

import com.example.movieapp.models.MovieResponse
import com.example.movieapp.network.MovieService
import com.example.movieapp.util.ResponseHandler
import com.example.movieapp.util.fetchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
