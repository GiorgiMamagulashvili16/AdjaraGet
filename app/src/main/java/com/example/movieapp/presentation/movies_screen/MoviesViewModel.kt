package com.example.movieapp.presentation.movies_screen

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.models.Movie
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.util.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepo: MovieRepositoryImpl
) : ViewModel(), SetChipState {

    private val _chipState = MutableStateFlow(buildVariantChipState)
    val chipState: StateFlow<ChipState> = _chipState

    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }

    private val _result = MutableStateFlow(MovieScreenState())
    val result: StateFlow<MovieScreenState> = _result

    fun getMovies() = viewModelScope.launch {
        d("CHIPSTATE", "${_chipState.value}")
        when (_chipState.value) {
            is ChipState.TopRated -> {
                fetchTopRatedMovies()
            }
            is ChipState.Popular -> {
                fetchPopularMovies()
            }
            is ChipState.Saved -> {
                fetchTopRatedMovies()
            }
        }
    }

    private suspend fun fetchPopularMovies() {
        movieRepo.getPopularMovies().cachedIn(viewModelScope).collect {
            _result.value = MovieScreenState(data = it)
        }
    }

    private suspend fun fetchTopRatedMovies() {
        movieRepo.getTopRatedMovies().cachedIn(viewModelScope).collect {
            _result.value = MovieScreenState(data = it)
        }
    }
}

