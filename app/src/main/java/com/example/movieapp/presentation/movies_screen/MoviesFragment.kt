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

    private var hasInternet: Boolean? = null

    private var isLastPage = false
    private var isScrolling = false

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
        lifecycleScope.launch {
            viewModel.result.collect { state ->
                if (!state.isLoading)
                    dismissLoadingDialog()
                else
                    showLoadingDialog()
                if (state.data != null) {
                    dismissLoadingDialog()
                    movieAdapter.submitList(state.data.results)
                    val totalPages = state.data.totalPages
                    isLastPage = viewModel.currentPage == totalPages

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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val currentItem = layoutManager.childCount
            val totalItems = layoutManager.itemCount
            val scrollOutItem = layoutManager.findFirstVisibleItemPosition()
            val isAtLastItem = currentItem + scrollOutItem >= totalItems
            if (isScrolling && isAtLastItem && !isLastPage) {
                viewModel.getMovies()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
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
            addOnScrollListener(scrollListener)
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
                        viewModel.changeCurrentPage(DEFAULT_PAGE_INDEX)
                        getMovies()
                    }
                    R.id.chpSaved -> {
                        viewModel.setChipState(ChipState.Saved)

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
                dismissLoadingDialog()
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
    }
}