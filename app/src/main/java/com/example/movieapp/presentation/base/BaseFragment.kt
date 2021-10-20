package com.example.movieapp.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.movieapp.databinding.DialogErrorBinding
import com.example.movieapp.databinding.DialogLoadingBinding
import com.example.movieapp.presentation.extensions.setDialog
import com.example.movieapp.util.Inflate


abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private var errorDialog: Dialog? = null
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    abstract fun initFragment()

    protected fun showErrorDialog(message: String, onRetryClick: () -> Unit) {
        errorDialog = Dialog(requireContext())
        val binding = DialogErrorBinding.inflate(layoutInflater)
        errorDialog!!.setDialog(binding)
        with(binding) {
            btnRetry.setOnClickListener {
                onRetryClick()
            }
            tvErrorText.text = message
        }
        errorDialog?.show()
    }

    protected fun dismissErrorDialog() {
        errorDialog?.dismiss()
    }

    protected fun showLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        val binding = DialogLoadingBinding.inflate(layoutInflater)
        loadingDialog!!.setDialog(binding)
        loadingDialog?.show()
    }

    protected fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}