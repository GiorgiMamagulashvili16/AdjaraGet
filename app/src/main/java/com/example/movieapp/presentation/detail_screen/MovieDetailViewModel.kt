package com.example.movieapp.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    private val _result = MutableStateFlow<Resource<MovieDetailResponse>>(Resource.Loading())
    val result: StateFlow<Resource<MovieDetailResponse>> = _result

    fun getMovieById(movieId: Int) = viewModelScope.launch {
        _result.value = movieRepository.getMovieById(movieId)
    }
}