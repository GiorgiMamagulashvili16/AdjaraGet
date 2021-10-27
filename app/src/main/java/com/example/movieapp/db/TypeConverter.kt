package com.example.movieapp.db

import androidx.room.TypeConverter
import com.example.movieapp.models.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {

    @TypeConverter
    fun toList(value: String): List<Genre> {
        val type = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromList(list: List<Genre>): String {
        return Gson().toJson(list)
    }

//    @TypeConverter
//    fun toGenre(value: String): Genre {
//        return Genre(value)
//    }
//
//    @TypeConverter
//    fun fromGenre(genre: Genre): String {
//        return genre.name
//    }
}