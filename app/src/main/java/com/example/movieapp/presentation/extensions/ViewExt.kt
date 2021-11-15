package com.example.movieapp.presentation.extensions

import android.view.View
import android.view.animation.*


fun View.setAnim(time: Long, animation: Int, eventAfterEnd: () -> Unit) {
    val anim = AnimationUtils.loadAnimation(context, animation).apply {
        duration = time
    }
    startAnimation(anim)
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) {
            eventAfterEnd()
        }

        override fun onAnimationRepeat(p0: Animation?) {}

    })
}