package com.example.movieapp.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.models.Error
import com.example.movieapp.models.Movie
import com.example.movieapp.network.MovieService
import com.example.movieapp.util.Resource
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class TopRatedMovies(val movieService: MovieService) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = movieService.getTopRatedMovies(currentPage)
            val data = mutableListOf<Movie>()
            val responseData = response.body()?.results ?: emptyList()
            data.addAll(responseData)
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (data.isEmpty()) null else currentPage + 1
            LoadResult.Page(data, prevPage, nextPage)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}