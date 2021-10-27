package com.example.movieapp.presentation.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

fun ByteArray.fromByteArrayToBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun Bitmap.fromBitmapToByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

fun <T> List<T>.fromListToString(): String {
    return Gson().toJson(this)
}

fun <T> String.fromStringToList(): List<T> {
    val type = object : TypeToken<List<T>>() {}.type
    return Gson().fromJson(this, type)
}