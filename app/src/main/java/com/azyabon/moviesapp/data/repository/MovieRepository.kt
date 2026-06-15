package com.azyabon.moviesapp.data.repository

import com.azyabon.moviesapp.data.local.dao.FavoriteMovieDao
import com.azyabon.moviesapp.data.local.entity.FavoriteMovieEntity
import com.azyabon.moviesapp.data.mapper.toMovie
import com.azyabon.moviesapp.data.mapper.toMovieDetails
import com.azyabon.moviesapp.data.remote.MovieApiService
import com.azyabon.moviesapp.domain.model.Movie
import com.azyabon.moviesapp.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val api: MovieApiService,
    private val favoriteMovieDao: FavoriteMovieDao,
) {

    suspend fun getPopularMovies(): List<Movie> {
        return api.getPopularMovies().results.map { it.toMovie() }
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        return api.getTopRatedMovies().results.map { it.toMovie() }
    }

    suspend fun getUpcomingMovies(): List<Movie> {
        return api.getUpcomingMovies().results.map { it.toMovie() }
    }

    suspend fun getNowPlayingMovies(): List<Movie> {
        return api.getNowPlayingMovies().results.map { it.toMovie() }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId).toMovieDetails()
    }

    fun observeFavoriteMovies(): Flow<List<FavoriteMovieEntity>> {
        return favoriteMovieDao.observeFavoriteMovies()
    }

    fun observeIsFavorite(movieId: Int): Flow<Boolean> {
        return favoriteMovieDao.observeIsFavorite(movieId)
    }

    suspend fun addToFavorites(movie: MovieDetails) {
        favoriteMovieDao.addToFavorites(
            FavoriteMovieEntity(
                id = movie.id,
                posterPath = movie.posterPath,
                rating = movie.voteAverage,
            )
        )
    }

    suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.removeFromFavorites(movieId)
    }
}