package com.azyabon.moviesapp.presentation.common

import androidx.annotation.ColorRes
import com.azyabon.moviesapp.R

@ColorRes
fun getRatingColor(rating: Double): Int {
    return when {
        rating >= 7.0 -> R.color.success
        rating >= 5.0 -> R.color.white
        else -> R.color.error
    }
}

@ColorRes
fun getRatingBackgroundColor(rating: Double): Int {
    return when {
        rating >= 7.0 -> R.color.success
        rating >= 5.0 -> R.color.black
        else -> R.color.error
    }
}