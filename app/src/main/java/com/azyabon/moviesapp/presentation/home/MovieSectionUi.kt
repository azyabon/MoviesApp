package com.azyabon.moviesapp.presentation.home

import com.azyabon.moviesapp.presentation.common.model.MovieUi

data class MovieSectionUi(
    val title: String,
    val type: MovieSectionType,
    val movies: List<MovieUi>
)
