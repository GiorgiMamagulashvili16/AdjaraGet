package com.example.movieapp.db

import android.graphics.Bitmap
import androidx.room.TypeConverter
import com.example.movieapp.models.Genre
import com.example.movieapp.presentation.extensions.fromBitmapToByteArray
import com.example.movieapp.presentation.extensions.fromByteArrayToBitmap
import com.example.movieapp.presentation.extensions.fromListToString
import com.example.movieapp.presentation.extensions.fromStringToList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {

    @TypeConverter
    fun toList(value: String) = value.fromStringToList<Genre>()

    @TypeConverter
    fun fromList(list: List<Genre>) = list.fromListToString()

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap = byteArray.fromByteArrayToBitmap()

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray = bitmap.fromBitmapToByteArray()
}