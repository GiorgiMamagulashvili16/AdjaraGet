package com.example.movieapp.presentation.detail_screen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<MovieDetailFragmentBinding>(MovieDetailFragmentBinding::inflate) {

    private val genreAdapter: GenresAdapter by lazy { GenresAdapter() }
    private val vm: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()


    override fun initFragment() {
        val movie = args.movie
        setDetailInfo(movie)
        setListeners()
        initGenreRecycle()
        binding.fabSave.setOnClickListener {
            vm.saveMovie(movie)
        }
    }

    private fun setDetailInfo(movie: Movie) {
        with(binding) {
            ivPoster.loadImage(IMAGE_URL + movie.poster_path)
            tvTitle.text = movie.title
            tvOriginalTitle.text = movie.original_title
            tvOverview.text = movie.overview
            rbMovieRating.rating = movie.vote_average.toFloat() / 2
            tvRating.text = movie.vote_average.toString()
            tvReleaseDate.text =
                getString(string.release_date_text, "Release Date:", movie.release_date)
        }
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val loader = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
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