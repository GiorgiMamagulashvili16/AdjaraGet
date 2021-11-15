package com.example.movieapp.presentation.splash_screen

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.SplashFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.setAnim
import com.example.movieapp.util.Constants.LOGO_ANIM_DURATION
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.anim
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<SplashFragmentBinding, SplashViewModel>() {

    override fun onBindViewModel(viewModel: SplashViewModel) {
        observeAnim(viewModel)
        binding.apply {
            ivLogo.setAnim(LOGO_ANIM_DURATION, anim.logo_anim) {
                viewModel.isAnimOver(true)
            }
        }
    }

    override fun getVmClass(): Class<SplashViewModel> = SplashViewModel::class.java

    private fun observeAnim(viewModel: SplashViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moveToNextFragment.collect { isAnimationOver ->
                if (isAnimationOver)
                    findNavController().navigate(R.id.action_splashFragment_to_moviesFragment)
            }
        }
    }
    override fun inflateFragment(): Inflate<SplashFragmentBinding> = SplashFragmentBinding::inflate
}