package com.example.movieapp.di

import com.example.movieapp.network.MovieService
import com.example.movieapp.network.interceptors.ErrorHandlingInterceptor
import com.example.movieapp.network.interceptors.NetworkConnectionInterceptor
import com.example.movieapp.network.interceptors.QueryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/"

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(QueryInterceptor())
        .addInterceptor(NetworkConnectionInterceptor())
        .addInterceptor(ErrorHandlingInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideGsonConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideHttpClient())
        .addConverterFactory(provideGsonConvertFactory())
        .build()

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}