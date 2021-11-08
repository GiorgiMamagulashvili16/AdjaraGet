package com.example.movieapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.movieapp.R
import com.example.movieapp.models.Movie

typealias anim = R.anim
typealias mipmap = R.mipmap
typealias drawable = R.drawable
typealias string = R.string
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
typealias onPosterClick = (movie:Movie) -> Unit