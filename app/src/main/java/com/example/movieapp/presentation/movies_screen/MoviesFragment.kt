package com.example.movieapp.presentation.movies_screen

import android.content.res.Configuration
import android.graphics.Color
import android.util.Log.d
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.adapters.MovieLoadStateAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.createSnackBar
import com.example.movieapp.util.NetworkConnectionChecker
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val movieAdapter by lazy { MovieAdapter() }
    private var isLandscapeMode: Boolean = false

    private var hasInternet: Boolean? = null
    override fun initFragment() {
        observeNetworkConnection()
        getScreenOrientationInfo()
        setChipsValue()
        chipSelect()
        setListeners()
        initRecycleView()
        observeResult()
        getMovies()
    }

    private fun observeNetworkConnection() {
        NetworkConnectionChecker(requireContext()).observe(viewLifecycleOwner, { it ->
            hasInternet = it
            d("STATE", "$it")
            getMovies()
        })

    }

    private fun loadState() {
        movieAdapter.addLoadStateListener { state ->
            when (state.source.refresh) {
                is LoadState.Loading -> d("LOADSTATE","loading")
                is LoadState.Error -> {
                    d("LOADSTATE","error")
                    showErrorDialog(
                        (state.source.refresh as LoadState.Error).error.localizedMessage!!,
                        onRetryClick = {
                            movieAdapter.retry()
                            dismissErrorDialog()
                        })
                    dismissLoadingDialog()
                }
                is LoadState.NotLoading ->  d("LOADSTATE","not load")
            }
        }
    }

    private fun getScreenOrientationInfo() {
        val orientation = requireActivity().resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            isLandscapeMode = true
    }

    private fun observeResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collect { state ->
                if (state.data != null) {
                    movieAdapter.submitData(viewLifecycleOwner.lifecycle, state.data)
                    dismissLoadingDialog()
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
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { movieAdapter.retry() },
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )

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
                        getMovies()
                    }
                    R.id.chpSaved -> {
                        viewModel.setChipState(ChipState.Saved)
                        getMovies()
                    }
                    R.id.chpTopRated -> {
                        viewModel.setChipState(ChipState.TopRated)
                        getMovies()
                    }
                }
            }
    }

    private fun getMovies() {
        when (hasInternet) {
            true -> {
                viewModel.getMovies()
            }
            null -> {
                showLoadingDialog()
            }
            false -> {
                dismissLoadingDialog()
                observeNetworkConnection()
                showErrorDialog(
                    getString(string.no_internet),
                    btnText = getString(string.go_to_saved),
                    onRetryClick = {
                        dismissErrorDialog()
                        viewModel.setChipState(ChipState.Saved)
                    })
            }
        }
        loadState()
    }

}