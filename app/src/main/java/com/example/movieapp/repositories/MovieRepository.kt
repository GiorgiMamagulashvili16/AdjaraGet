package com.example.movieapp.repositories

import com.example.movieapp.models.Movie
import com.example.movieapp.models.MovieResponse
import com.example.movieapp.util.Resource

interface MovieRepository {
   suspend  fun getTopRatedMovies(page:Int): Resource<MovieResponse>
   suspend  fun getPopular(page:Int): Resource<MovieResponse>

}