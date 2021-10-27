package com.example.movieapp.presentation.extensions

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide

fun String.imageToBitmap(context: Context): Bitmap {
    return Glide.with(context).asBitmap().load(this).submit().get()
}