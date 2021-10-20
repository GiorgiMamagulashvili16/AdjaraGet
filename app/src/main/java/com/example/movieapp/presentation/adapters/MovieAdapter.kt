package com.example.movieapp.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.RowMovieItemBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.IsLastItem
import com.example.movieapp.util.onPosterClick

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.VH>() {

    lateinit var isLastItem: IsLastItem
    lateinit var onPosterClick: onPosterClick

    val movieList = mutableListOf<Movie>()

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(movie = movieList[position])
        holder.binding.root.setOnClickListener {
            onPosterClick.invoke(movieList[position].id)
        }
        if (position == movieList.size - 2) {
            isLastItem.invoke(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            RowMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class VH(val binding: RowMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(movie: Movie) {
            with(binding) {
                root
                root.loadImage(IMAGE_URL + movie.poster_path)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun insertList(newList: List<Movie>) {
        this.movieList.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadMore(movieList: List<Movie>) {
        this.movieList.addAll(movieList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        this.movieList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}