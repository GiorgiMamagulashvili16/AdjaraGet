package com.example.movieapp.presentation.detail_screen

import android.util.Log.d
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
import com.example.movieapp.presentation.extensions.observeData
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
            binding.ivPoster.loadImage(it)
        })
        viewModel.title.observe(viewLifecycleOwner, {
            binding.tvTitle.text = it
        })
        viewModel.releaseDate.observe(viewLifecycleOwner, {
            binding.tvReleaseDate.text = it
        })
        viewModel.coverUrl.observe(viewLifecycleOwner, {
            binding.ivCover?.loadImage(it)
        })
        viewModel.originalTitle.observe(viewLifecycleOwner, {
            binding.tvOriginalTitle.text = it
        })
        viewModel.movieRating.observe(viewLifecycleOwner, {
            binding.tvRating.text = it
        })
        viewModel.rating.observe(viewLifecycleOwner, {
            binding.rbMovieRating.rating = it
        })
        viewModel.overView.observe(viewLifecycleOwner, {
            binding.tvOverview.text = it
        })
        observeData(viewModel.title){
            d("OBSERVEDDATA", it)
        }
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


