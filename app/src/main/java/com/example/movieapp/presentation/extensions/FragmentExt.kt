package com.example.movieapp.presentation.extensions

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

fun Fragment.createSnackBar(message: String, color: Int) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        setTextColor(color)
    }.show()
}

fun <T> Fragment.observeData(liveData: LiveData<T>, observer: (it: T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, { observer(it) })
}

fun Fragment.isLandScape(): Boolean {
    val orientation = requireActivity().resources.configuration.orientation
    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        return true
    return false
}