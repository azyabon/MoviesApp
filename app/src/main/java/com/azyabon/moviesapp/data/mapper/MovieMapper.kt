package com.azyabon.moviesapp.data.mapper

import com.azyabon.moviesapp.data.remote.dto.GenreDto
import com.azyabon.moviesapp.data.remote.dto.MovieDetailsDto
import com.azyabon.moviesapp.data.remote.dto.MovieListItemDto
import com.azyabon.moviesapp.domain.model.Genre
import com.azyabon.moviesapp.domain.model.Movie
import com.azyabon.moviesapp.domain.model.MovieDetails

fun MovieListItemDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview.orEmpty(),
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date.orEmpty(),
        voteAverage = vote_average,
        genreIds = genre_ids
    )
}

fun MovieDetailsDto.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview.orEmpty(),
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date.orEmpty(),
        voteAverage = vote_average,
        runtime = runtime,
        genres = genres.map { it.toGenre() },
        tagline = tagline.orEmpty(),
        homepage = homepage
    )
}

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}
