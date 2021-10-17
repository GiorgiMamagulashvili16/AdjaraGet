package com.example.movieapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.RowChipItemBinding
import com.example.movieapp.models.Genre

class GenresAdapter : ListAdapter<Genre, GenresAdapter.VH>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        RowChipItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.onBind()


    inner class VH(val binding: RowChipItemBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var genre: Genre
        fun onBind() {
            genre = getItem(adapterPosition)
            binding.root.text = genre.name
        }
    }

    object COMPARATOR : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }

    }
}