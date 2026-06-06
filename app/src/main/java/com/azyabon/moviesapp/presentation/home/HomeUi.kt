package com.azyabon.moviesapp.presentation.home

data class HomeUi(
    val isLoading: Boolean = false,
    val sections: List<MovieSectionUi> = emptyList(),
    val errorMessage: String? = null
)
