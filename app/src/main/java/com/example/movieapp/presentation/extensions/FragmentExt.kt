package com.example.movieapp.presentation.extensions

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.movieapp.databinding.DialogErrorBinding
import com.google.android.material.snackbar.Snackbar

fun Fragment.createSnackBar(message: String, color: Int) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        setTextColor(color)
    }.show()
}

fun <T> Fragment.observeData(liveData: LiveData<T>, observer: (it: T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, { observer(it) })
}

fun Dialog.showError(
    message: String,
    onRetryClick: () -> Unit,
    btnText: String
) {
    val binding = DialogErrorBinding.inflate(layoutInflater)
    this.setDialog(binding)
    with(binding) {
        btnRetry.text = btnText
        btnRetry.setOnClickListener {
            onRetryClick()
        }
        tvErrorText.text = message
    }
    this.show()
}

fun Dialog.dismissDialog() {
    this.hide()
}