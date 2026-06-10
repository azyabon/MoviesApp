package com.azyabon.moviesapp.presentation.movie

import com.azyabon.moviesapp.domain.model.MovieDetails

data class MovieUi(
    val isLoading: Boolean = false,
    val movie: MovieDetails? = null,
    val errorMessage: String? = null
)
