package com.example.movieapp.presentation.detail_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment

class MovieDetailFragment :
    BaseFragment<MovieDetailFragmentBinding>(MovieDetailFragmentBinding::inflate) {

    private val genreAdapter: GenresAdapter by lazy { GenresAdapter() }

    override fun initFragment(layoutInflater: LayoutInflater, viewGroup: ViewGroup?) {
        setListeners()
        setGenres()
        initGenreRecycle()
    }

    private fun setListeners() {
        binding.apply {
            ibBack.setOnClickListener {
                findNavController().navigate(R.id.action_movieDetailFragment_to_moviesFragment)
            }
        }
        binding.ivPoster.clipToOutline = true
    }

    private fun initGenreRecycle() {
        binding.rvGenres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    private fun setGenres() {
        genreAdapter.submitList(
            mutableListOf(
                Genre(1, "action"),
                Genre(2, "anim"),
                Genre(3, "Fantasy")
            )
        )
    }

}