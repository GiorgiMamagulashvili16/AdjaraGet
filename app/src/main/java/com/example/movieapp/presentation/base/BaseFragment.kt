package com.example.movieapp.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.movieapp.databinding.DialogErrorBinding
import com.example.movieapp.databinding.DialogLoadingBinding
import com.example.movieapp.presentation.extensions.*
import com.example.movieapp.util.Inflate
import com.example.movieapp.util.NetworkConnectionChecker
import com.example.movieapp.util.string
import java.lang.Exception


abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>() : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private lateinit var viewModel: VM
    private var errorDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(getVmClass())
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
    abstract fun setListeners()

}