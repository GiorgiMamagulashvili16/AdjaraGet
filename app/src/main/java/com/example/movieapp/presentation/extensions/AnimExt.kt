package com.example.movieapp.presentation.extensions

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator


fun View.setAnim(time: Long, animation: Int) {
    val anim = AnimationUtils.loadAnimation(context, animation).apply {
        duration = time
    }
    startAnimation(anim)
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