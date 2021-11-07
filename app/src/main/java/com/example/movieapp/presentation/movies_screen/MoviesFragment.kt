package com.example.movieapp.presentation.movies_screen

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.*
import com.example.movieapp.util.Constants.DEFAULT_PAGE_INDEX
import com.example.movieapp.util.Constants.PAGE_SIZE
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.string
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment() :
    BaseFragment<MoviesFragmentBinding, MoviesViewModel>() {

    override var isSharedVm: Boolean = true
    override fun getVmClass(): Class<MoviesViewModel> = MoviesViewModel::class.java
    override fun inflateFragment(): Inflate<MoviesFragmentBinding> = MoviesFragmentBinding::inflate

    private val movieAdapter by lazy { MovieAdapter() }
    override fun onBindViewModel(viewModel: MoviesViewModel) {
        viewModel.getMovies()
        observeResult(viewModel)
        setChipsValue(viewModel)
        chipSelect(viewModel)
        getScreenOrientationInfo(viewModel)
        initRecycleView(viewModel)
    }

    private fun getScreenOrientationInfo(viewModel: MoviesViewModel) {
        viewModel.setLandScape(isLandScape())
    }

    private fun observeResult(viewModel: MoviesViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest { state ->
                if (!state.isLoading)
                    binding.progressBar.hide()
                else
                    binding.progressBar.show()
                if (state.data != null) {

                    binding.tvEmpty.isVisible = state.data.isEmpty()
                    movieAdapter.submitList(state.data)
                }
                if (state.error != null) {
                    createSnackBar(state.error, length = Snackbar.LENGTH_INDEFINITE) {
                        snackAction(textColor = Color.RED, action = getString(string.go_to_saved)) {
                            viewModel.setChipState(ChipState.Saved)
                        }
                    }
                }
            }
        }
    }

    private fun initRecycleView(viewModel: MoviesViewModel) {
        movieAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        observeData(viewModel.isLandscape) { isLandScape ->

            val lm = if (isLandScape) GridLayoutManager(
                requireContext(),
                LANDSCAPE_SPAN_COUNT
            ) else GridLayoutManager(
                requireContext(),
                PORTRAIT_SPAN_COUNT
            )

            with(binding.rvMovies) {
                layoutManager = lm
                adapter = movieAdapter
                addOnScrollListener(
                    OnScrollListener(
                        { viewModel.getMovies() },
                        viewModel.isLastPage,
                        PAGE_SIZE,
                        lm
                    )
                )
            }

        }

    }

    private fun setChipsValue(viewModel: MoviesViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chipState.collect { state ->
                with(binding) {
                    when (state) {
                        is ChipState.Popular -> {
                            chpPopular.isChecked = true
                        }
                        is ChipState.TopRated -> {
                            chpTopRated.isChecked = true
                        }
                        is ChipState.Saved -> chpSaved.isChecked = true
                    }
                }
            }
        }
    }

    private fun chipSelect(viewModel: MoviesViewModel) {
        binding.chpGroup
            .setOnCheckedChangeListener { _, checkedId ->
                with(viewModel) {
                    when (checkedId) {
                        R.id.chpPopular -> {
                            setChipState(ChipState.Popular)
                            changeCurrentPage(DEFAULT_PAGE_INDEX)
                            getMovies()
                        }
                        R.id.chpSaved -> {
                            setChipState(ChipState.Saved)
                            getMovies()
                        }
                        R.id.chpTopRated -> {
                            setChipState(ChipState.TopRated)
                            changeCurrentPage(DEFAULT_PAGE_INDEX)
                            getMovies()
                        }
                    }
                }
            }
    }

    override fun setListeners() {
        movieAdapter.onPosterClick = { movie ->
            with(findNavController()) {
                if (currentDestination?.id == R.id.moviesFragment) {
                    val action =
                        MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(movie)
                    navigate(action)
                }
            }

        }
    }

    companion object {
        private const val PORTRAIT_SPAN_COUNT = 2
        private const val LANDSCAPE_SPAN_COUNT = 3
    }
}