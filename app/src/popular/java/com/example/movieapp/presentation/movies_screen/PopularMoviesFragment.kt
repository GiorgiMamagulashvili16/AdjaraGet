package com.example.movieapp.presentation.movies_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PopularMoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate) {

    private val viewModel: PopularMoviesViewModel by activityViewModels()

    override fun initFragment() {
        setChipsValue()
        chipSelect()
        setListeners()
    }

    private fun setListeners() {
        binding.toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_movies_fragment_to_movieDetailFragment)
        }
    }

    private fun setChipsValue() {
        lifecycleScope.launch {
            viewModel.chipState.collect { state ->
                when (state) {
                    is ChipState.Popular -> binding.chpPopular.isChecked = true
                    is ChipState.TopRated -> binding.chpTopRated.isChecked = true
                    is ChipState.Saved -> binding.chpSaved.isChecked = true
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