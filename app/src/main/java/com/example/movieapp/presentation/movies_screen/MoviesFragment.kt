package com.example.movieapp.presentation.movies_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.R
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.presentation.base.BaseFragment

class MoviesFragment : BaseFragment<MoviesFragmentBinding>(MoviesFragmentBinding::inflate){

    override fun initFragment(layoutInflater: LayoutInflater, viewGroup: ViewGroup?) {
        init()
    }
    private fun init(){

    }

}