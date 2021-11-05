package com.example.movieapp.presentation.extensions

import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun ProgressBar.show() {
    this.isVisible = true
}

fun ProgressBar.hide() {
    this.isVisible = false
}
