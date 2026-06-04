package com.azyabon.moviesapp.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val runtime: Int?,
    val genres: List<Genre>,
    val tagline: String,
    val homepage: String?
)
