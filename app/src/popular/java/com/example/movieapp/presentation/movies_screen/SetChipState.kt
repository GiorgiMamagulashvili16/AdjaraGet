package com.example.movieapp.presentation.movies_screen

interface SetChipState {

    val buildVariant: ChipState
        get() = ChipState.Popular
}