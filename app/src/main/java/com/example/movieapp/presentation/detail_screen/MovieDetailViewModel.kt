package com.example.movieapp.presentation.detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.Movie
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.SavedMovieRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl,
    private val savedMovieRepo: SavedMovieRepoImpl
) : ViewModel() {
    var isSavedMovie = false

    fun saveMovie(movie: Movie) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savedMovieRepo.addMovie(movie)
        }
    }

    fun isMovieSaved(id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val response = savedMovieRepo.isMovieSaved(id)
            isSavedMovie = response
        }
    }

    fun removeMovie(id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savedMovieRepo.removeMovie(id)
        }
    }
}