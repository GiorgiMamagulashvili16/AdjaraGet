package com.example.movieapp.db

import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.models.Movie

@Database(entities = [Movie::class], version = 1,)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao
}