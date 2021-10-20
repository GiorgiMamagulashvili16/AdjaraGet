package com.example.movieapp.presentation.detail_screen

import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailFragmentBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.models.MovieDetailResponse
import com.example.movieapp.presentation.adapters.GenresAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.loadImage
import com.example.movieapp.util.Constants.IMAGE_URL
import com.example.movieapp.util.Resource
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        lifecycleScope.launch {
            vm.result.collectLatest { result ->
                when (result) {
                    is Resource.Success -> setDetailInfo(result.data!!)
                }
            }
        }
    }

    private fun setDetailInfo(movie: MovieDetailResponse) {
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