package com.example.movieapp.di

import com.example.movieapp.network.MovieService
import com.example.movieapp.repositories.MovieRepository
import com.example.movieapp.repositories.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepo(movieService: MovieService): MovieRepository =
        MovieRepositoryImpl(movieService)
}