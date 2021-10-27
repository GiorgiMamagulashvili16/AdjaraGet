package com.example.movieapp.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.Movie
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.SavedMovieRepoImpl
import com.example.movieapp.util.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl,
    private val savedMovieRepo: SavedMovieRepoImpl
) : ViewModel() {

    private val _result = MutableStateFlow(DetailScreenState())
    val result: StateFlow<DetailScreenState> = _result

    fun getMovieById(movieId: Int) = viewModelScope.launch {
        _result.value = DetailScreenState(isLoading = true)
        val response = movieRepository.getMovieById(movieId)
        when (response) {
            is ResponseHandler.Success -> _result.value =
                DetailScreenState(isLoading = false, data = response.data)
            is ResponseHandler.Error -> _result.value =
                DetailScreenState(isLoading = false, error = response.errorMessage)
        }
    }
    fun saveMovie(movie:Movie) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            savedMovieRepo.addMovie(movie)
        }
    }
}