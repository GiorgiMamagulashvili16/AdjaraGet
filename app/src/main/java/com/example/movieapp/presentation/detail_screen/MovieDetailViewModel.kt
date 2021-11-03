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
import dagger.hilt.android.qualifiers.ApplicationContext
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
    var isSavedMovie = false

    private var _movie: MutableLiveData<Movie> = MutableLiveData()
    val movie: LiveData<Movie> = _movie
    private var _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> = _title
    private var _originalTitle: MutableLiveData<String> = MutableLiveData()
    val originalTitle: LiveData<String> = _originalTitle
    private var _rating: MutableLiveData<Float> = MutableLiveData()
    val rating: LiveData<Float> = _rating
    private var _overView: MutableLiveData<String> = MutableLiveData()
    val overView: LiveData<String> = _overView
    private var _posterUrl: MutableLiveData<String> = MutableLiveData()
    val posterUrl: LiveData<String> = _posterUrl
    private var _coverUrl: MutableLiveData<String> = MutableLiveData()
    val coverUrl: LiveData<String> = _coverUrl
    private var _releaseDate: MutableLiveData<String> = MutableLiveData()
    val releaseDate: LiveData<String> = _releaseDate
    private var _movieRating: MutableLiveData<String> = MutableLiveData()
    val movieRating: LiveData<String> = _movieRating


    fun setMovie(movie: Movie) = viewModelScope.launch {
        _movie.postValue(movie)
        setMovieDetails()
    }

    private fun setMovieDetails() = viewModelScope.launch {
        _coverUrl.postValue(_movie.value?.getBackDropUrl())
        _movieRating.postValue(_movie.value?.vote_average.toString())
        _originalTitle.postValue(_movie.value?.original_title)
        _title.postValue(_movie.value?.title)
        _rating.postValue(_movie.value?.vote_average?.toFloat()?.div(2))
        _releaseDate.postValue(
            resourcesProvider.getString(
                string.release_date_text,
                "Release Date: ",
                _movie.value?.release_date
            )
        )
        _overView.postValue(_movie.value?.overview)
        _posterUrl.postValue(_movie.value?.getPosterUrl())
    }

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