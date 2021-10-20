package com.example.movieapp.presentation.movies_screen

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.Error
import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieResponse
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

    private val _result = MutableStateFlow<Resource<MovieResponse>>(Resource.Loading())
    val result: StateFlow<Resource<MovieResponse>> = _result

    fun getMovies(page: Int) = viewModelScope.launch {
        when (_chipState.value) {
            is ChipState.TopRated -> {
                _result.value = movieRepo.getTopRatedMovies(page)
            }
            is ChipState.Popular -> {
                _result.value = movieRepo.getPopular(page)
            }
        }
    }

}