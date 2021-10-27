package com.example.movieapp.presentation.detail_screen

import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.hide
import com.example.movieapp.presentation.extensions.imageToBitmap
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.presentation.extensions.show
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.drawable
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<MovieDetailFragmentBinding>(MovieDetailFragmentBinding::inflate) {

    private val genreAdapter: GenresAdapter by lazy { GenresAdapter() }
    private val vm: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()


    override fun initFragment() {
        vm.getMovieById(args.movieId)

        setListeners()
        initGenreRecycle()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            vm.result.collect { state ->
                if (state.isLoading)
                    binding.progressBar.show()
                else
                    binding.progressBar.hide()
                if (state.error != null)
                    showErrorDialog(state.error, onRetryClick = {
                        vm.getMovieById(args.movieId)
                        dismissErrorDialog()
                    })
                if (state.data != null) {
                    setDetailInfo(state.data)
                    saveMovie(state.data)
                }
            }
        }
    }

    private fun setDetailInfo(movie: Movie) {
        with(binding) {
            ivPoster.loadImage(IMAGE_URL + movie.posterPath)
            tvTitle.text = movie.title
            tvOriginalTitle.text = movie.originalTitle
            tvOverview.text = movie.overview
            ivCover?.apply {
                clipToOutline = true
                loadImage(IMAGE_URL + movie.coverPath)
            }
            rbMovieRating.rating = movie.rating.toFloat() / 2
            tvRating.text = movie.rating.toString()
            setGenres(movie.genres)
            tvReleaseDate.text =
                getString(string.release_date_text, "Release Date:", movie.releaseDate)
        }
    }

    private fun saveMovie(movie: Movie?) {
        movie?.let {
            val favouriteMovie = Movie(
                it.id,
                coverPath = null,
                it.genres,
                it.originalTitle,
                it.overview,
                posterPath = null,
                releaseDate = it.releaseDate,
                title = it.title,
                rating = it.rating,
                poster = (IMAGE_URL + it.posterPath).imageToBitmap(requireContext()),
                cover = (IMAGE_URL + it.posterPath).imageToBitmap(requireContext())
            )
            binding.fabSave.setOnClickListener {
                vm.saveMovie(favouriteMovie)
            }
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