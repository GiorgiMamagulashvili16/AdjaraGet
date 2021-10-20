package com.example.movieapp.di

import com.example.movieapp.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = "https://api.themoviedb.org/"

    @Provides
    @Singleton
    fun provideMovieService(): MovieService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieService::class.java)
}