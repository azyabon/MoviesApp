package com.azyabon.moviesapp.presentation.home

import androidx.lifecycle.ViewModel
import com.azyabon.moviesapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.azyabon.moviesapp.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUi())
    val uiState: StateFlow<HomeUi> = _uiState

    init {
        loadPopularMovies()
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUi(isLoading = true)

            try {
                val popular = movieRepository.getPopularMovies()
                val topRated = movieRepository.getTopRatedMovies()
                val upcoming = movieRepository.getUpcomingMovies()

                _uiState.value = HomeUi(
                    sections = listOf(
                        MovieSectionUi(
                            title = "Popular",
                            type = MovieSectionType.POPULAR,
                            movies = popular.map { it.toUi() }
                        ),
                        MovieSectionUi(
                            title = "Top Rated",
                            type = MovieSectionType.TOP_RATED,
                            movies = topRated.map { it.toUi() }
                        ),
                        MovieSectionUi(
                            title = "Upcoming",
                            type = MovieSectionType.UPCOMING,
                            movies = upcoming.map { it.toUi() }
                        )
                    )
                )
            } catch (e: Exception) {
                _uiState.value = HomeUi(errorMessage = e.message)
            }
        }
    }

    private fun Movie.toUi(): MovieUi {
        return MovieUi(
            id = id,
            rating = voteAverage,
            posterPath = posterPath
        )
    }
}