package com.example.movieapp.presentation.movies_fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.movies_screen.ChipState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TopRatedMoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: TopRatedMoviesViewModel by activityViewModels()

    override fun initFragment() {
        setChipsValue()
        chipSelect()
        setListeners()
    }

    private fun setListeners() {
        binding.toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_moviesFragment_to_movieDetailFragment)
        }
    }

    private fun setChipsValue() {
        lifecycleScope.launch {
            viewModel.chipState.collect {
                when (it) {
                    ChipState.TopRated -> binding.chpTopRated.isChecked = true
                    ChipState.Saved -> binding.chpSaved.isChecked = true
                    ChipState.Popular -> binding.chpPopular.isChecked = true
                }
            }
        }
    }

    private fun chipSelect() {
        binding.chpGroup
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.chpPopular -> viewModel.setChipState(ChipState.Popular)
                    R.id.chpSaved -> viewModel.setChipState(ChipState.Saved)
                    R.id.chpTopRated -> viewModel.setChipState(ChipState.TopRated)
                }
            }
    }

}