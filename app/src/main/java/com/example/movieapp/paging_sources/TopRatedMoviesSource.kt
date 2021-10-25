package com.example.movieapp.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.models.Movie
import com.example.movieapp.network.MovieService
import com.example.movieapp.util.Constants.DEFAULT_PAGE_INDEX
import com.example.movieapp.util.Constants.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class TopRatedMoviesSource(private val movieService: MovieService) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: DEFAULT_PAGE_INDEX
            val response = movieService.getTopRatedMovies(currentPage)
            val dataList = mutableListOf<Movie>()
            val data = response.body()?.results ?: emptyList()
            dataList.addAll(data)
            val prevKey = if (currentPage == 1) null else currentPage - 1
            val nextKey =
                if (dataList.isEmpty()) null else currentPage + (params.loadSize / PAGE_SIZE)
            LoadResult.Page(
                dataList, prevKey, nextKey
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}