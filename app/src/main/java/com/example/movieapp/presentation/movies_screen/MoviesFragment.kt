package com.example.movieapp.presentation.movies_screen

import android.content.res.Configuration
import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.createSnackBar
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val movieAdapter by lazy { MovieAdapter() }
    private var totalPages: Int = 0
    private var currentPage = 1
    private var isLandscapeMode: Boolean = false

    override fun initFragment() {
        getScreenOrientationInfo()
        setChipsValue()
        chipSelect()
        setListeners()
        initRecycleView()
        observeResult()
        currentPage = 1
        paginate()
        movieFirstLoad()
    }

    private fun movieFirstLoad() {
        viewModel.getMovies(currentPage)
        movieAdapter.clearList()
    }

    private fun getScreenOrientationInfo() {
        val orientation = requireActivity().resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            isLandscapeMode = true
    }

    private fun paginate() {
        movieAdapter.isLastItem = {
            if (it) {
                currentPage++
                if (currentPage != totalPages) {
                    viewModel.getMovies(currentPage)
                } else {
                    createSnackBar(getString(string.no_more_data), Color.WHITE)
                }
            }
        }
    }

    private fun observeResult() {
        lifecycleScope.launchWhenCreated {
            viewModel.result.collect { state ->
                if (state.isLoading) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
                if (state.data.isNotEmpty()) {
                    if (movieAdapter.movieList.isEmpty()) {
                        movieAdapter.insertList(state.data)
                    } else {
                        movieAdapter.loadMore(state.data)
                    }
                }
                if (state.error != null) {
                    showErrorDialog(state.error) {
                        viewModel.getMovies(currentPage)
                        dismissErrorDialog()
                    }
                }
            }
        }
    }

    private fun initRecycleView() {
        binding.rvMovies.apply {
            layoutManager =
                if (isLandscapeMode) GridLayoutManager(requireContext(), 3) else GridLayoutManager(
                    requireContext(),
                    2
                )
            adapter = movieAdapter

        }
    }

    private fun setListeners() {
        movieAdapter.onPosterClick = { id ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(id)
            findNavController().navigate(action)
        }
    }

    private fun setChipsValue() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chipState.collect { state ->
                when (state) {
                    is ChipState.Popular -> {
                        binding.chpPopular.isChecked = true
                    }
                    is ChipState.TopRated -> {
                        binding.chpTopRated.isChecked = true
                    }
                    is ChipState.Saved -> binding.chpSaved.isChecked = true
                }
            }
        }
    }

    private fun chipSelect() {
        binding.chpGroup
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.chpPopular -> {
                        viewModel.setChipState(ChipState.Popular)
                        currentPage =1
                        viewModel.getMovies(currentPage)
                        movieAdapter.clearList()
                    }
                    R.id.chpSaved -> {
                        viewModel.setChipState(ChipState.Saved)
                    }
                    R.id.chpTopRated -> {
                        viewModel.setChipState(ChipState.TopRated)
                        currentPage =1
                        viewModel.getMovies(currentPage)
                        movieAdapter.clearList()
                    }
                }
            }
    }

}