package com.azyabon.moviesapp.data.repository

import com.azyabon.moviesapp.data.mapper.toMovie
import com.azyabon.moviesapp.data.mapper.toMovieDetails
import com.azyabon.moviesapp.data.remote.MovieApiService
import com.azyabon.moviesapp.domain.model.Movie
import com.azyabon.moviesapp.domain.model.MovieDetails

class MovieRepository(
    private val api: MovieApiService
) {

    suspend fun getPopularMovies(): List<Movie> {
        return api.getPopularMovies().results.map { it.toMovie() }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId).toMovieDetails()
    }
}