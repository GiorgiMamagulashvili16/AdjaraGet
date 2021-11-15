package com.example.movieapp.presentation.detail_screen

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.presentation.extensions.observeData
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.drawable
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment() :
    BaseFragment<MovieDetailFragmentBinding, MovieDetailViewModel>() {

    private val args: MovieDetailFragmentArgs by navArgs()
    override fun inflateFragment(): Inflate<MovieDetailFragmentBinding> =
        MovieDetailFragmentBinding::inflate

    override fun getVmClass(): Class<MovieDetailViewModel> = MovieDetailViewModel::class.java

    override fun onBindViewModel(viewModel: MovieDetailViewModel) {
        val movie = args.movie
        viewModel.setMovie(movie)
        viewModel.isMovieSaved(movie.id)
        setFab(viewModel)
        observeMovie(viewModel)
        observeMovieDetails(viewModel)
    }

    private fun observeMovieDetails(viewModel: MovieDetailViewModel) {
        with(viewModel) {
            with(binding) {
                observeData(movieDetails) { movie ->
                    tvOverview.text = movie.overview
                    tvRating.text = movie.vote_average.toString()
                    tvReleaseDate.text = movie.release_date
                    tvTitle.text = movie.title
                    tvOriginalTitle.text = movie.original_title
                    ivPoster.loadImage(movie.poster_path)
                    ivCover?.loadImage(movie.backdrop_path)
                }
            }
        }
        observeMovieRating(viewModel)
    }

    private fun observeMovieRating(viewModel: MovieDetailViewModel) {
        observeData(viewModel.rating) {
            binding.rbMovieRating.rating = it
        }
    }

    private fun observeMovie(viewModel: MovieDetailViewModel) {
        with(viewModel) {
            observeData(movie) {
                setMovieDetails(it)
                setFabClickListener(it, viewModel)
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


