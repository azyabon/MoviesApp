package com.azyabon.moviesapp.presentation.favorites

import com.azyabon.moviesapp.presentation.common.model.MovieUi

data class FavoritesUi(
    val movies: List<MovieUi> = emptyList()
)
