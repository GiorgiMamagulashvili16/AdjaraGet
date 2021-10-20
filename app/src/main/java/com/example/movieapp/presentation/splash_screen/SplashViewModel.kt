package com.example.movieapp.presentation.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _moveToNextFragment = MutableStateFlow(false)
    val moveToNextFragment :StateFlow<Boolean> = _moveToNextFragment

    fun isAnimOver(state:Boolean) = viewModelScope.launch {
        _moveToNextFragment.value = state
    }
}