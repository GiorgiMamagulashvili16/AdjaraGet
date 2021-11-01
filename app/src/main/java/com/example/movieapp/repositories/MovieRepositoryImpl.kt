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
    override suspend fun getMovieById(movieId: Int): ResponseHandler<Movie> {

        return withContext(Dispatchers.IO) {
            fetchData {
                val response = movieService.getMovieById(movieId = movieId)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            }
        }
    }

    override suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse> {
        return withContext(Dispatchers.IO) {
            fetchData {
                val response = movieService.getTopRatedMovies(page)
                if (response.isSuccessful) {
                    val body = response.body()
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            }
        }
    }

    override suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse> {
        return withContext(Dispatchers.IO) {
            fetchData {
                val response = movieService.getPopularMovies(page)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            }
        }
    }
}
