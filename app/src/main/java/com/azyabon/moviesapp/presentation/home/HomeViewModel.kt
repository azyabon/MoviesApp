package com.azyabon.moviesapp.presentation.home

import androidx.lifecycle.ViewModel
import com.azyabon.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadPopularMovies()
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            try {
                val movies = movieRepository.getPopularMovies()
                _uiState.value = HomeUiState(movies = movies)
            } catch (e: Exception) {
                _uiState.value = HomeUiState(errorMessage = e.message)
            }
        }
    }
}