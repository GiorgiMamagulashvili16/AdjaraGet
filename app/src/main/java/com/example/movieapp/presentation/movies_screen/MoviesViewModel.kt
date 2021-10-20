package com.example.movieapp.presentation.movies_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel(),SetChipState {
    private val _chipState = MutableStateFlow<ChipState>(buildVariant)
    val chipState: StateFlow<ChipState> = _chipState

    fun setChipState(state: ChipState) = viewModelScope.launch {
        _chipState.value = state
    }

    private var chipStat: ChipState = ChipState.Popular

}