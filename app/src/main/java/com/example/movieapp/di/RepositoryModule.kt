package com.example.movieapp.di

import com.example.movieapp.db.MoviesDao
import com.example.movieapp.network.MovieService
import com.example.movieapp.repositories.MovieRepository
import com.example.movieapp.repositories.MovieRepositoryImpl
import com.example.movieapp.repositories.saved_movies.SavedMovieRepoImpl
import com.example.movieapp.repositories.saved_movies.SavedMoviesRepository
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

    @Provides
    @Singleton
    fun provideSavedMovieRepo(dao: MoviesDao): SavedMoviesRepository = SavedMovieRepoImpl(dao)
}