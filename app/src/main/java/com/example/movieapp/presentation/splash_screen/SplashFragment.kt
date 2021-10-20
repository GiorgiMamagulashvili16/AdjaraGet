package com.example.movieapp.presentation.splash_screen

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.SplashFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.setAnim
import com.example.movieapp.util.Constants.LOGO_ANIM_DURATION
import com.example.movieapp.util.anim
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<SplashFragmentBinding>(SplashFragmentBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()
    override fun initFragment() {

        binding.apply {
            ivLogo.setAnim(LOGO_ANIM_DURATION, anim.logo_anim) {
                viewModel.isAnimOver(true)
            }

        }
        lifecycleScope.launch {
            viewModel.moveToNextFragment.collect {isAnimationOver->
                if (isAnimationOver)
                    findNavController().navigate(R.id.action_splashFragment_to_moviesFragment)
            }
        }
    }

}