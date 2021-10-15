package com.example.movieapp.presentation.splash_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.anim
import com.example.movieapp.databinding.SplashFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.setAnim
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<SplashFragmentBinding>(SplashFragmentBinding::inflate) {


    override fun onStart(layoutInflater: LayoutInflater, viewGroup: ViewGroup?) {
        init()
    }
    private fun init(){
        binding.apply {
            ivLogo.setAnim(2000,anim.logo_anim)
        }

        lifecycleScope.launch {
            delay(2500)
            findNavController().navigate(R.id.action_splashFragment_to_moviesFragment)
        }
    }
}