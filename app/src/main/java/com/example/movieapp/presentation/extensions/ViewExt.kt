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

fun View.fadeIn(time: Int) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.apply {
        interpolator = DecelerateInterpolator()
        duration = time.toLong()
    }
    this.startAnimation(fadeIn)
}

fun View.fadeOut(time: Int) {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.apply {
        interpolator = AccelerateInterpolator()
        duration = time.toLong()
    }
    this.startAnimation(fadeOut)
}