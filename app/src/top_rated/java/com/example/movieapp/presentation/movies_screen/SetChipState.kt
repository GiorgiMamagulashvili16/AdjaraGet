package com.example.movieapp.presentation.movies_screen

interface SetChipState {
    val buildVariantChipState: ChipState
        get() = ChipState.TopRated
}