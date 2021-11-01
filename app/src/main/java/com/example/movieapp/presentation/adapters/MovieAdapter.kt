package com.example.movieapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.RowMovieItemBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.IsLastItem
import com.example.movieapp.util.onPosterClick

class MovieAdapter : ListAdapter<Movie, MovieAdapter.VH>(COMPARATOR) {

    lateinit var onPosterClick: onPosterClick

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.onBind(item!!)
        holder.binding.root.setOnClickListener {
            onPosterClick.invoke(item)
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
            binding.root.loadImage(movie.getPosterUrl())
        }
    }

    object COMPARATOR : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem

    }

}