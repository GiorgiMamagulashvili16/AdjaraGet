package com.example.movieapp.presentation.movies_screen

sealed class ChipState {
    object TopRated : ChipState()
    object Popular : ChipState()
    object Saved : ChipState()
}
