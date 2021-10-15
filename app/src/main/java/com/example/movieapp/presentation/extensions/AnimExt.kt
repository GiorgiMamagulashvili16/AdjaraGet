package com.example.movieapp.presentation.extensions

import android.view.View
import android.view.animation.AnimationUtils


fun View.setAnim(time: Long, animation: Int) {
    val anim = AnimationUtils.loadAnimation(context, animation).apply {
        duration = time
    }
    startAnimation(anim)
}