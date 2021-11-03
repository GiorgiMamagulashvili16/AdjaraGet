package com.example.movieapp.presentation.movies_screen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.SavedMovieRepoImpl
import com.example.movieapp.util.NetworkConnectionChecker
import com.example.movieapp.util.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepo: MovieRepositoryImpl,
    private val savedMovieRepo: SavedMovieRepoImpl,
    @ApplicationContext app: Context
) : ViewModel(), SetChipState {

    private val _chipState = MutableStateFlow(buildVariantChipState)
    val chipState: StateFlow<ChipState> = _chipState

    private val _isLandscape: MutableLiveData<Boolean> = MutableLiveData()
    val isLandscape: LiveData<Boolean> = _isLandscape

    private val _hasInternetConnection = MutableStateFlow<Boolean?>(null)
    val hasInternetConnection: StateFlow<Boolean?> = _hasInternetConnection

    fun setInternetConnection(newValue: Boolean) = viewModelScope.launch {
        _hasInternetConnection.value = newValue
    }
    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }

    fun setLandScape(newValue: Boolean) = viewModelScope.launch {
        _isLandscape.postValue(newValue)
    }

    val connectionChecker = NetworkConnectionChecker(app)

    private var topRatedMovieResponse: MovieResponse? = null
    private var popularMovieResponse: MovieResponse? = null
    private var currentPage = 0
    private var lastPage = 1

    var isLastPage = currentPage == lastPage

    fun changeCurrentPage(newValue: Int) = viewModelScope.launch {
        currentPage = newValue
    }

    private val _result = MutableStateFlow(MovieScreenState())
    val result: StateFlow<MovieScreenState> = _result

    fun getMovies() {
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

    private fun getSavedMovies() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _result.value = MovieScreenState(data = savedMovieRepo.getAllMovies())
        }
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
                lastPage = popularMovieResponse?.totalPages!!
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
                lastPage = topRatedMovieResponse?.totalPages!!
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

