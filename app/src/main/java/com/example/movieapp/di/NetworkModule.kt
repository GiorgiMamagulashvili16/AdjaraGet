package com.example.movieapp.di

import com.example.movieapp.network.MovieService
import com.example.movieapp.util.Constants.API_KEY
import com.example.movieapp.util.Constants.API_KEY_QUERY_PARAM
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val BASE_URL = "https://api.themoviedb.org/"

    private fun okHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val url = request.url().newBuilder().addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                    .build()
                request = request.newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)
            })
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}