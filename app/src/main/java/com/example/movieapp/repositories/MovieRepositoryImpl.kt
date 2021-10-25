package com.example.movieapp.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.models.Error
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.network.MovieService
import com.example.movieapp.paging_sources.PopularMoviesSource
import com.example.movieapp.paging_sources.TopRatedMoviesSource
import com.example.movieapp.util.Constants.PAGE_SIZE
import com.example.movieapp.util.ResponseHandler
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository {
    override  fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { TopRatedMoviesSource(movieService) }).flow

    }

    override  fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { PopularMoviesSource(movieService) }).flow
    }

    override suspend fun getMovieById(movieId: Int): ResponseHandler<MovieDetailResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = movieService.getMovieById(movieId = movieId)
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
