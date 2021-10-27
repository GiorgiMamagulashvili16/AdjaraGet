package com.example.movieapp.presentation.extensions

import android.widget.ProgressBar
import androidx.core.view.isVisible

fun ProgressBar.hide() {
    this.isVisible = false
}

fun ProgressBar.show() {
    this.isVisible = true
}