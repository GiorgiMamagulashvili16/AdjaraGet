package com.example.movieapp.presentation.extensions

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.example.movieapp.databinding.DialogErrorBinding

fun Dialog.setDialog(binding: ViewBinding) {
    window!!.setBackgroundDrawableResource(android.R.color.transparent)
    window!!.requestFeature(Window.FEATURE_NO_TITLE)
    val params = this.window!!.attributes
    params.width = WindowManager.LayoutParams.MATCH_PARENT
    params.height = WindowManager.LayoutParams.WRAP_CONTENT
    setContentView(binding.root)
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