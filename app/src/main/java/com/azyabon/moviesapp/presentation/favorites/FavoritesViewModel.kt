package com.azyabon.moviesapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azyabon.moviesapp.data.local.entity.FavoriteMovieEntity
import com.azyabon.moviesapp.data.repository.MovieRepository
import com.azyabon.moviesapp.presentation.common.model.MovieUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUi())
    val uiState: StateFlow<FavoritesUi> = _uiState

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            movieRepository.observeFavoriteMovies().collect { movies ->
                _uiState.value = FavoritesUi(
                    movies = movies.map { it.toUi() }
                )
            }
        }
    }

    private fun FavoriteMovieEntity.toUi(): MovieUi {
        return MovieUi(
            id = id,
            rating = rating,
            posterPath = posterPath
        )
    }
}