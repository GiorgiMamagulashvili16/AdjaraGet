package com.example.movieapp.presentation.detail_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.Movie
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.SavedMovieRepoImpl
import com.example.movieapp.util.ResourcesProvider
import com.example.movieapp.util.string
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl,
    private val savedMovieRepo: SavedMovieRepoImpl,
    private val resourcesProvider: ResourcesProvider,
) : ViewModel() {
    private var _isMovieSaved: MutableLiveData<Boolean> = MutableLiveData()
    val isMovieSaved: LiveData<Boolean> = _isMovieSaved


    private var _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie> = _movie
    private var _movieDetails: MutableLiveData<Movie> = MutableLiveData()
    val movieDetails: LiveData<Movie> = _movieDetails
    private var _rating: MutableLiveData<Float> = MutableLiveData()
    val rating: LiveData<Float> = _rating

    fun setMovie(movie: Movie) = viewModelScope.launch {
        _movie.postValue(movie)
    }

    fun setMovieDetails(movie: Movie) = viewModelScope.launch {
        val releaseDate = resourcesProvider.getString(
            string.release_date_text,
            string.release_date,
            movie.release_date
        )
        val movieDetails = Movie(
            id = movie.id,
            backdrop_path = movie.getBackDropUrl(),
            original_title = movie.original_title,
            overview = movie.overview,
            poster_path = movie.getPosterUrl(),
            release_date = releaseDate,
            title = movie.title,
            vote_average = movie.vote_average
        )
        _rating.postValue(movie.vote_average.toFloat().div(2))
        _movieDetails.postValue(movieDetails)
    }

    fun changeIsMovieSaved(newValue: Boolean) = viewModelScope.launch {
        _isMovieSaved.postValue(newValue)
    }

    fun saveMovie(movie: Movie) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savedMovieRepo.addMovie(movie)
        }
    }

    fun isMovieSaved(id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val response = savedMovieRepo.isMovieSaved(id)
            _isMovieSaved.postValue(response)
        }
    }

    fun removeMovie(id: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savedMovieRepo.removeMovie(id)
        }
    }

}