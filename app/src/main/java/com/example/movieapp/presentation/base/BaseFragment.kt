package com.example.movieapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.movieapp.util.Inflate


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(layoutInflater, container, false)
        initFragment(layoutInflater, container)
        return binding.root
    }

    abstract fun initFragment(layoutInflater: LayoutInflater, viewGroup: ViewGroup?)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}