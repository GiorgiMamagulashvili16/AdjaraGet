package com.example.movieapp.presentation.movies_screen

import android.content.res.Configuration
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.hide
import com.example.movieapp.presentation.extensions.show
import com.example.movieapp.util.Constants.CONNECTION_TIME
import com.example.movieapp.util.Constants.DEFAULT_PAGE_INDEX
import com.example.movieapp.util.Constants.PAGE_SIZE
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesFragmentBinding, MoviesViewModel>() {


    override fun getVmClass(): Class<MoviesViewModel> = MoviesViewModel::class.java
    override fun inflateFragment(): Inflate<MoviesFragmentBinding> = MoviesFragmentBinding::inflate

    private val movieAdapter by lazy { MovieAdapter() }

    override fun onBindViewModel(viewModel: MoviesViewModel) {
        getMovies(viewModel)
        observeResult(viewModel)
        setChipsValue(viewModel)
        observeNetworkConnection(viewModel)
        chipSelect(viewModel)
        getScreenOrientationInfo(viewModel)
        initRecycleView(viewModel)
    }

    private fun observeNetworkConnection(viewModel: MoviesViewModel) {
        viewModel.connectionChecker.observe(viewLifecycleOwner, {
            viewModel.hasInternetConnection = it
            getMovies(viewModel)
        })
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CONNECTION_TIME)
            if (viewModel.hasInternetConnection == null) {
                viewModel.hasInternetConnection = false
                getMovies(viewModel)
            }
        }
    }

    private fun getScreenOrientationInfo(viewModel: MoviesViewModel) {
        val orientation = requireActivity().resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            viewModel.isLandScape = true
    }

    private fun observeResult(viewModel: MoviesViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collect { state ->
                if (!state.isLoading)
                    binding.progressBar.hide()
                else
                    binding.progressBar.show()
                if (state.data != null) {
                    binding.tvEmpty.isVisible = state.data.isEmpty()
                    movieAdapter.submitList(state.data)

                }
                if (state.error != null) {
                    showErrorDialog(state.error, onRetryClick = {
                        viewModel.changeCurrentPage(DEFAULT_PAGE_INDEX)
                        viewModel.getMovies()
                    })
                }
            }
        }
    }

    private fun initRecycleView(viewModel: MoviesViewModel) {
        with(binding.rvMovies) {
            layoutManager =
                if (viewModel.isLandScape) GridLayoutManager(
                    requireContext(),
                    3
                ) else GridLayoutManager(
                    requireContext(),
                    2
                )
            adapter = movieAdapter
            addOnScrollListener(
                OnScrollListener(
                    { getMovies(viewModel) },
                    viewModel.isLastPage,
                    PAGE_SIZE
                )
            )
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

    private fun getMovies(viewModel: MoviesViewModel) {
        when (viewModel.hasInternetConnection) {
            true -> {
                binding.progressBar.hide()
                viewModel.getMovies()
            }
            null -> {
                binding.progressBar.show()
                observeNetworkConnection(viewModel)
            }
            false -> {
                binding.progressBar.hide()
                showErrorDialog(
                    getString(string.no_internet),
                    btnText = getString(string.go_to_saved),
                    onRetryClick = {
                        dismissErrorDialog()
                        viewModel.setChipState(ChipState.Saved)
                    })
            }
        }
    }

    override fun setListeners() {
        movieAdapter.onPosterClick = { movie ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(movie)
            findNavController().navigate(action)
        }
    }


}