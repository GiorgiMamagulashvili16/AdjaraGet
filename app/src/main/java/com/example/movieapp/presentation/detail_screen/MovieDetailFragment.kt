package com.example.movieapp.presentation.detail_screen

import android.view.LayoutInflater
import android.view.ViewGroup
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
    BaseFragment<MovieDetailFragmentBinding, MovieDetailViewModel>() {

    private val genreAdapter: GenresAdapter by lazy { GenresAdapter() }
    private val args: MovieDetailFragmentArgs by navArgs()


    override fun inflateFragment(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): MovieDetailFragmentBinding =
        MovieDetailFragmentBinding.inflate(layoutInflater, viewGroup, false)

    override fun getVmClass(): Class<MovieDetailViewModel> = MovieDetailViewModel::class.java

    override fun initFragment() {
        val movie = args.movie
        viewModel.getMovie(movie)
        viewModel.isMovieSaved(movie.id)
        setListeners()
        initGenreRecycle()
        setFab()
        setFabClickListener(movie)
    }

    override fun onBindViewModel(viewModel: MovieDetailViewModel) {

        viewModel.posterUrl.observe(viewLifecycleOwner, {
            setPoster(it)
        })
        viewModel.title.observe(viewLifecycleOwner, {
            setTitle(it)
        })
        viewModel.releaseDate.observe(viewLifecycleOwner, {
            setReleaseDate(it)
        })
    }

    private fun setPoster(url: String) {
        binding.ivPoster.loadImage(url)
    }

    private fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    private fun setReleaseDate(date: String) {
        binding.tvReleaseDate.text = date
    }

    private fun setFabClickListener(movie: Movie) {
        binding.fabSave.setOnClickListener {
            if (viewModel.isSavedMovie)
                viewModel.removeMovie(movie.id)
            else
                viewModel.saveMovie(movie)
            viewModel.isSavedMovie = !viewModel.isSavedMovie
            setFab()
        }
    }

    private fun setFab() {
        val isSaved = viewModel.isSavedMovie
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