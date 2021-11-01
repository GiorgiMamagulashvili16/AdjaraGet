package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.models.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie)

    @Query("SELECT * FROM movies_table ORDER BY id DESC")
    fun getMovies(): List<Movie>

    @Query("SELECT EXISTS(SELECT * FROM movies_table WHERE id = :id)")
    fun isMovieSaved(id: Int): Boolean

    @Query("DELETE FROM movies_table WHERE id = :id")
    suspend fun removeMovieById(id: Int);
}