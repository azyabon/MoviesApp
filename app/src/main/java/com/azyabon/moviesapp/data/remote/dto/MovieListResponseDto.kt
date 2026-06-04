package com.azyabon.moviesapp.data.remote.dto

data class MovieListResponseDto(
    val page: Int,
    val results: List<MovieListItemDto>,
    val total_pages: Int,
    val total_results: Int
)
