package com.example.movieapp.presentation.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.createSnackBar(message: String, color: Int) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        setTextColor(color)
    }.show()
}