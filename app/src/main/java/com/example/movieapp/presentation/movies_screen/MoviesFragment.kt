package com.example.movieapp.presentation.movies_screen

import android.util.Log.d
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.presentation.adapters.MovieAdapter
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: MoviesViewModel by activityViewModels()

    private val movieAdapter by lazy { MovieAdapter() }
    private var currentPage = 1
    private var totalPages: Int= 0
    private var isFirstLoad = true

    override fun initFragment() {
        setChipsValue()
        chipSelect()
        setListeners()
        initRecycleView()
        observeResult()
        paginate()
        viewModel.getMovies(currentPage)
    }

    private fun paginate() {
        movieAdapter.isLastItem = {
            if (it) {
                isFirstLoad = false
                currentPage++
                if ( currentPage != totalPages)
                    viewModel.getMovies(currentPage)
            }
        }
    }

    private fun observeResult() {
        lifecycleScope.launch {
            viewModel.result.collectLatest { state ->
                when (state) {
                    is Resource.Success -> if (movieAdapter.movieList.isEmpty()) {
                        val result = state.data?.results!!
                        movieAdapter.insertList(result)
                        totalPages = state.data.totalPages
                    } else {
                        movieAdapter.loadMore(state.data?.results!!)
                    }
                    is Resource.Error -> d("STATE", "error")
                    is Resource.Loading -> d("STATE", "Loadign")
                }
            }
        }
    }

    private fun initRecycleView() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter

        }
    }

    private fun setListeners() {
        binding.toolbar.root.setOnClickListener {
//            findNavController().navigate(R.id.action_moviesFragment_to_movieDetailFragment)
        }
    }

    private fun setChipsValue() {
        lifecycleScope.launch {
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
                        currentPage = 1
                        viewModel.getMovies(currentPage)
                        movieAdapter.clearList()
                    }
                    R.id.chpSaved -> {
                        viewModel.setChipState(ChipState.Saved)
                    }
                    R.id.chpTopRated -> {
                        viewModel.setChipState(ChipState.TopRated)
                        currentPage = 1
                        viewModel.getMovies(currentPage)
                        movieAdapter.clearList()
                    }
                }
            }
    }

}