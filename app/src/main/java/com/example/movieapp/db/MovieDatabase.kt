package com.example.movieapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.models.Movie

@Database(entities = [Movie::class], version = 2)
@TypeConverters(TypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MoviesDao
}