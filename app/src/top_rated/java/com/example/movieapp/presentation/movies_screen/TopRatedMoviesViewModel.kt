package com.example.movieapp.presentation.movies_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.presentation.movies_screen.ChipState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopRatedMoviesViewModel : ViewModel() {

    private val _chipState = MutableStateFlow<ChipState>(ChipState.TopRated)
    val chipState: StateFlow<ChipState> = _chipState

    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }
}