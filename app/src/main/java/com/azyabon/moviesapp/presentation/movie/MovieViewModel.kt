package com.azyabon.moviesapp.presentation.movie

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
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            _uiState.value = MovieUi(isLoading = true)

            try {
                val movie = movieRepository.getMovieDetails(movieId)

                _uiState.value = MovieUi(
                    movie = movie,
                )
            } catch (e: Exception) {
                _uiState.value = MovieUi(errorMessage = e.message)
            }
        }
    }
}