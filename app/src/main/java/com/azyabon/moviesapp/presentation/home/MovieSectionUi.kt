package com.azyabon.moviesapp.presentation.home

data class MovieSectionUi(
    val title: String,
    val type: MovieSectionType,
    val movies: List<MovieUi>
)
