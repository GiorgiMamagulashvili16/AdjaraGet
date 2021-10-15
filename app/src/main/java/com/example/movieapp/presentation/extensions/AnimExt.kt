package com.example.movieapp.presentation.extensions

import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import com.example.movieapp.anim


fun View.setAnim(time:Long, animation:Int){
    val anim = AnimationUtils.loadAnimation(context, animation).apply {
        duration = time
        interpolator = BounceInterpolator()
    }
    startAnimation(anim)
}