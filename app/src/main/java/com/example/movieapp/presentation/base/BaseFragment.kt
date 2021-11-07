package com.example.movieapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.movieapp.util.Inflate

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>() :
    Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    open var isSharedVm: Boolean = false
    private lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = if (isSharedVm) {
            ViewModelProvider(requireActivity()).get(getVmClass())
        } else {
            ViewModelProvider(this).get(getVmClass())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateFragment().invoke(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindViewModel(viewModel)
        setListeners()

    }


    abstract fun inflateFragment(): Inflate<VB>
    abstract fun getVmClass(): Class<VM>
    abstract fun onBindViewModel(viewModel: VM)
    open fun setListeners() {}

}