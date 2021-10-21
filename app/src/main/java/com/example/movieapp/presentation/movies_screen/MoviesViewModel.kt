package com.example.movieapp.presentation.movies_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepo: MovieRepositoryImpl
) : ViewModel(), SetChipState {
    private val _chipState = MutableStateFlow<ChipState>(buildVariantChipState)
    val chipState: StateFlow<ChipState> = _chipState

    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }

    private val _result = MutableStateFlow(MovieScreenState())
    val result: StateFlow<MovieScreenState> = _result

    fun getMovies(page: Int) = viewModelScope.launch {

        when (_chipState.value) {
            is ChipState.TopRated -> {
                getTopRatedMovies(page)
            }
            is ChipState.Popular -> {
                getPopularMovies(page)
            }
            else -> Unit
        }
    }

    private suspend fun getTopRatedMovies(page: Int) = viewModelScope.launch {
        _result.value = MovieScreenState(isLoading = true)
        val response = movieRepo.getTopRatedMovies(page)
        when (response) {
            is Resource.Success -> _result.value =
                MovieScreenState(isLoading = false, data = response.data?.results!!)
            is Resource.Error -> _result.value =
                MovieScreenState(isLoading = false, error = response.errorMessage)
        }
    }

    private suspend fun getPopularMovies(page: Int) = viewModelScope.launch {
        _result.value = MovieScreenState(isLoading = true)
        val response = movieRepo.getPopular(page)
        when (response) {
            is Resource.Success -> _result.value =
                MovieScreenState(isLoading = false, data = response.data?.results!!)
            is Resource.Error -> _result.value =
                MovieScreenState(isLoading = false, error = response.errorMessage)
        }
    }
}

