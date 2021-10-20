package com.example.movieapp.presentation.extensions

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.example.movieapp.R

fun AppCompatImageView.loadImage(url:String){
    Glide.with(this.context).load(url).into(this)
}