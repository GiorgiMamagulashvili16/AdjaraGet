package com.example.movieapp.presentation.splash_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.SplashFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment
import com.example.movieapp.presentation.extensions.setAnim
import com.example.movieapp.util.Constants.LOGO_ANIM_DURATION
import com.example.movieapp.util.Constants.SPLASH_DELAY_TIME
import com.example.movieapp.util.anim
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<SplashFragmentBinding>(SplashFragmentBinding::inflate) {


    override fun initFragment() {

        binding.apply {
            ivLogo.setAnim(LOGO_ANIM_DURATION,anim.logo_anim)
        }
        lifecycleScope.launch {
            delay(SPLASH_DELAY_TIME)
            findNavController().navigate(R.id.action_splashFragment_to_movies_fragment)
        }
    }

}