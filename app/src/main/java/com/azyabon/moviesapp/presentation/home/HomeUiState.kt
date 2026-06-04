package com.azyabon.moviesapp.presentation.home

import com.azyabon.moviesapp.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null
)
