package com.example.movieapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val backdrop_path: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
) : Parcelable
