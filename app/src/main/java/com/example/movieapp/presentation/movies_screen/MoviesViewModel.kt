package com.example.movieapp.presentation.movies_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.saved_movies.SavedMovieRepoImpl
import com.example.movieapp.util.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepo: MovieRepositoryImpl,
    private val savedMovieRepo: SavedMovieRepoImpl
) : ViewModel(), SetChipState {

    private val _chipState = MutableStateFlow(buildVariantChipState)
    val chipState: StateFlow<ChipState> = _chipState

    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }

    private var topRatedMovieResponse: MovieResponse? = null
    private var popularMovieResponse: MovieResponse? = null
    var currentPage = 0

    fun changeCurrentPage(newValue: Int) = viewModelScope.launch {
        currentPage = newValue
    }

    private val _result = MutableStateFlow(MovieScreenState())
    val result: StateFlow<MovieScreenState> = _result

    fun getMovies() = viewModelScope.launch {
        currentPage++
        when (_chipState.value) {
            is ChipState.TopRated -> {
                fetchTopRatedMovies(currentPage)
            }
            is ChipState.Popular -> {
                fetchPopularMovies(currentPage)
            }
            is ChipState.Saved -> {
                getSavedMovies()
            }
        }
    }

    private fun getSavedMovies() = viewModelScope.launch{
        val result = savedMovieRepo.getMovies()
        _result.value = MovieScreenState(isLoading = false, data = result)
    }

    private fun fetchPopularMovies(page: Int) = viewModelScope.launch {
        _result.value = MovieScreenState(isLoading = true)
        val response = movieRepo.getPopularMovies(page)
        when (response) {
            is ResponseHandler.Success -> {
                if (popularMovieResponse == null) {
                    popularMovieResponse = response.data
                } else {
                    val oldList = popularMovieResponse?.results
                    val newList = response.data?.results
                    if (newList != null) {
                        oldList?.addAll(newList)
                    }
                }
                _result.value = MovieScreenState(
                    isLoading = false,
                    data = popularMovieResponse?.results ?: response.data!!.results
                )
            }
            is ResponseHandler.Error -> {
                _result.value = MovieScreenState(isLoading = false, error = response.errorMessage)
            }
        }
    }

    private fun fetchTopRatedMovies(page: Int) = viewModelScope.launch {
        _result.value = MovieScreenState(isLoading = true)
        val response = movieRepo.getTopRatedMovies(page)
        when (response) {
            is ResponseHandler.Success -> {
                if (topRatedMovieResponse == null) {
                    topRatedMovieResponse = response.data
                } else {
                    val oldList = topRatedMovieResponse?.results
                    val newList = response.data?.results
                    if (newList != null) {
                        oldList?.addAll(newList)
                    }
                }
                _result.value = MovieScreenState(
                    isLoading = false,
                    data = topRatedMovieResponse?.results ?: response.data!!.results
                )
            }
            is ResponseHandler.Error -> {
                _result.value = MovieScreenState(isLoading = false, error = response.errorMessage)
            }
        }
    }
}

