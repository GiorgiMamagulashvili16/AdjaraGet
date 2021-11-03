package com.example.movieapp.presentation.detail_screen

import android.util.Log.d
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.presentation.extensions.observeData
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.drawable
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<MovieDetailFragmentBinding, MovieDetailViewModel>() {

    private val args: MovieDetailFragmentArgs by navArgs()
    override fun inflateFragment(): Inflate<MovieDetailFragmentBinding> =
        MovieDetailFragmentBinding::inflate

    override fun getVmClass(): Class<MovieDetailViewModel> = MovieDetailViewModel::class.java

    override fun onBindViewModel(viewModel: MovieDetailViewModel) {
        val movie = args.movie
        viewModel.setMovie(movie)
        viewModel.isMovieSaved(movie.id)
        setFabClickListener(movie, viewModel)
        setFab(viewModel)
        with(viewModel) {
            with(binding) {
                observeData(title) {
                    tvTitle.text = it
                }
                observeData(originalTitle) {
                    tvOriginalTitle.text = it
                }
                observeData(releaseDate) {
                    tvReleaseDate.text = it
                }
                observeData(coverUrl) {
                    ivCover?.loadImage(it)
                }
                observeData(posterUrl) {
                    ivPoster.loadImage(it)
                }
                observeData(movieRating) {
                    tvRating.text = it
                }
                observeData(rating) {
                    rbMovieRating.rating = it
                }
                observeData(overView) {
                    tvOverview.text = it
                }
            }
        }

    }

    private fun setFabClickListener(movie: Movie, viewModel: MovieDetailViewModel) {
        observeData(viewModel.isMovieSaved) { isMovieSaved ->
            binding.fabSave.setOnClickListener {
                if (isMovieSaved)
                    viewModel.removeMovie(movie.id)
                else
                    viewModel.saveMovie(movie)
                viewModel.changeIsMovieSaved(!isMovieSaved)
                setFab(viewModel)
            }
        }

    }

    private fun setFab(viewModel: MovieDetailViewModel) {
        viewModel.isMovieSaved.observe(viewLifecycleOwner, {
            if (it) {
                setRemoveFab()
            } else {
                setSaveFab()
            }
        })
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

    override fun setListeners() {
        with(binding) {
            ibBack.setOnClickListener {
                findNavController().navigate(R.id.action_movieDetailFragment_to_moviesFragment)
            }
        }
    }


}


