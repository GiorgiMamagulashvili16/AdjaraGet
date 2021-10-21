package com.example.movieapp.repositories

import com.example.movieapp.models.Error
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.network.MovieService
import com.example.movieapp.util.ResponseHandler
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService
) : MovieRepository {
    override suspend fun getTopRatedMovies(page: Int): ResponseHandler<MovieResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.getTopRatedMovies(page)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            } catch (e: HttpException) {
                ResponseHandler.Error(e.message!!)
            }
        }

    override suspend fun getPopularMovies(page: Int): ResponseHandler<MovieResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.getPopularMovies(page)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            } catch (e: HttpException) {
                ResponseHandler.Error(e.localizedMessage!!)
            } catch (e: SocketTimeoutException) {
                ResponseHandler.Error(e.localizedMessage!!)
            }
        }

    override suspend fun getMovieById(movieId: Int): ResponseHandler<MovieDetailResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = service.getMovieById(movieId = movieId)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    ResponseHandler.Success(body)
                } else {
                    val errorBody =
                        Gson().fromJson(response.errorBody()!!.string(), Error::class.java)
                    ResponseHandler.Error(errorBody.statusMessage)
                }
            } catch (e: HttpException) {
                ResponseHandler.Error(e.message!!)
            }
        }
}
