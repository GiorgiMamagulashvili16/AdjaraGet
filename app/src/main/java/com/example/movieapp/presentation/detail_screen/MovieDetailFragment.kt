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
        setDetailInfo(movie)
        setListeners()
        initGenreRecycle()
        binding.fabSave.setOnClickListener {
            vm.saveMovie(movie)
        }
    }

//    private fun observeData() {
//        lifecycleScope.launchWhenCreated {
//            vm.result.collect { state ->
//                if (state.isLoading)
//                    showLoadingDialog()
//                else
//                    dismissLoadingDialog()
//                if (state.error != null)
//                    showErrorDialog(state.error, onRetryClick = {
//                        vm.getMovieById(args.movieId)
//                        dismissErrorDialog()
//                    })
//                if (state.data != null)
//                    setDetailInfo(state.data)
//            }
//        }
//    }

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