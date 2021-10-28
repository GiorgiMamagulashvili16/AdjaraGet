package com.example.movieapp.presentation.movies_screen

import android.content.res.Configuration
import android.util.Log.d
import android.widget.AbsListView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.hide
import com.example.movieapp.presentation.extensions.show
import com.example.movieapp.util.Constants.CONNECTION_TIME
import com.example.movieapp.util.Constants.DEFAULT_PAGE_INDEX
import com.example.movieapp.util.NetworkConnectionChecker
import com.example.movieapp.util.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val movieAdapter by lazy { MovieAdapter() }
    private var isLandscapeMode: Boolean = false

    private var hasInternet: Boolean? = true


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
        viewModel.connectionChecker.observe(viewLifecycleOwner, {
            hasInternet = it
            getMovies()
        })
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CONNECTION_TIME)
            if (hasInternet == null) {
                hasInternet = false
                getMovies()
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
                if (!state.isLoading)
                    binding.progressBar.hide()
                else
                    binding.progressBar.show()
                if (state.data != null) {
                    binding.progressBar.hide()
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

    private fun initRecycleView() {
        binding.rvMovies.apply {
            layoutManager =
                if (isLandscapeMode) GridLayoutManager(requireContext(), 3) else GridLayoutManager(
                    requireContext(),
                    2
                )
            adapter = movieAdapter
        }

        movieAdapter.isLastItem = {
            if (it && !viewModel.isLastPage)
                getMovies()
        }
    }

    private fun setListeners() {
        movieAdapter.onPosterClick = { movie ->
            val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailFragment(movie)
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
                        viewModel.changeCurrentPage(DEFAULT_PAGE_INDEX)
                        getMovies()
                    }
                    R.id.chpSaved -> {
                        viewModel.setChipState(ChipState.Saved)
                        viewModel.getMovies()
                    }
                    R.id.chpTopRated -> {
                        viewModel.setChipState(ChipState.TopRated)
                        viewModel.changeCurrentPage(DEFAULT_PAGE_INDEX)
                        getMovies()
                    }
                }
            }
    }

    private fun getMovies() {
        when (hasInternet) {
            true -> {
                binding.progressBar.hide()
                viewModel.getMovies()
            }
            null -> {
                binding.progressBar.show()
                observeNetworkConnection()
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
}