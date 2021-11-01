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

fun <T> List<T>.fromListToString(): String {
    return Gson().toJson(this)
}

fun <T> String.fromStringToList(): List<T> {
    val type = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(this, type)
}