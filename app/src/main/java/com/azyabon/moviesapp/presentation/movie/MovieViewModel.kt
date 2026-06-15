package com.azyabon.moviesapp.presentation.movie

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azyabon.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUi())
    val uiState: StateFlow<MovieUi> = _uiState

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    init {
        loadMovieDetail()
        observeFavoriteStatus()
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            movieRepository.observeIsFavorite(movieId).collect { isFavorite ->
                _uiState.value = _uiState.value.copy(isFavorite = isFavorite)
            }
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movie ?: return

        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                movieRepository.removeFromFavorites(movie.id)
            } else {
                movieRepository.addToFavorites(movie)
            }
        }
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val movie = movieRepository.getMovieDetails(movieId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}