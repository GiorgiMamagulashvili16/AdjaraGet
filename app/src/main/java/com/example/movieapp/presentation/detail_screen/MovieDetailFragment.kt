package com.example.movieapp.presentation.detail_screen

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.drawable
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<MovieDetailFragmentBinding>(MovieDetailFragmentBinding::inflate) {

    private val genreAdapter: GenresAdapter by lazy { GenresAdapter() }
    private val vm: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()
    override fun initFragment() {
        val movie = args.movie
        vm.isMovieSaved(movie.id)
        setDetailInfo(movie)
        setListeners()
        initGenreRecycle()
        setFab()
        setFabClickListener(movie)
    }

    private fun setFabClickListener(movie: Movie) {
        binding.fabSave.setOnClickListener {
            if (vm.isSavedMovie)
                vm.removeMovie(movie.id)
            else
                vm.saveMovie(movie)
            vm.isSavedMovie = !vm.isSavedMovie
            setFab()
        }
    }

    private fun setFab() {
        val isSaved = vm.isSavedMovie
        if (isSaved) {
            setRemoveFab()
        } else {
            setSaveFab()
        }
    }

    private fun setRemoveFab() {
        binding.fabSave.apply {
            text = getString(string.remove)
            setIconResource(drawable.ic_remove)
        }
    }

    private fun setSaveFab() {
        binding.fabSave.apply {
            text = getString(string.save)
            setIconResource(drawable.ic_add)
        }
    }

    private fun setDetailInfo(movie: Movie) {
        with(binding) {
            ivPoster.loadImage(IMAGE_URL + movie.poster_path)
            tvTitle.text = movie.title
            ivCover?.loadImage(IMAGE_URL + movie.backdrop_path)
            tvOriginalTitle.text = movie.original_title
            tvOverview.text = movie.overview
            rbMovieRating.rating = movie.vote_average.toFloat() / 2
            tvRating.text = movie.vote_average.toString()
            tvReleaseDate.text =
                getString(string.release_date_text, "Release Date:", movie.release_date)
        }
    }

    private fun setListeners() {
        with(binding) {
            ibBack.setOnClickListener {
                findNavController().navigate(R.id.action_movieDetailFragment_to_moviesFragment)
            }
            ivPoster.clipToOutline = true
        }
    }

    private fun initGenreRecycle() {
        binding.rvGenres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }

    private fun setGenres(genres: List<Genre>) {
        genreAdapter.submitList(genres)
    }
}