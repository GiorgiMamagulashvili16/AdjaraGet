package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::
    class
)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): MovieDatabase =
        Room.databaseBuilder(app, MovieDatabase::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun provideDao(db:MovieDatabase) = db.getMovieDao()
}