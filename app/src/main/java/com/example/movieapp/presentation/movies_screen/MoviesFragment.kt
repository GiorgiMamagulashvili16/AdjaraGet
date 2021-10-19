package com.example.movieapp.presentation.movies_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviesFragment : Fragment() {

    private var _binding:MoviesFragmentBinding ? =null
    private val binding get() = _binding!!


    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoviesFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setChipsValue()
        chipSelect()
        setListeners()
    }

    private fun setListeners(){
        binding.toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_moviesFragment_to_movieDetailFragment)
        }
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            supportActionBar?.hide()
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = null
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
            .setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.chpPopular -> viewModel.setChipState(ChipState.Popular)
                    R.id.chpSaved -> viewModel.setChipState(ChipState.Saved)
                    R.id.chpTopRated -> viewModel.setChipState(ChipState.TopRated)
                }
            }
    }
}