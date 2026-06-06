package com.azyabon.moviesapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azyabon.moviesapp.data.repository.MovieRepository
import com.azyabon.moviesapp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUi())
    val uiState: StateFlow<HomeUi> = _uiState

    init {
        loadPopularMovies()
    }

    private data class HomeData(
        val popular: List<Movie>,
        val topRated: List<Movie>,
        val upcoming: List<Movie>,
        val nowPlaying: List<Movie>
    )

    private fun loadPopularMovies() {
        viewModelScope.launch {
            _uiState.value = HomeUi(isLoading = true)

            try {
                val homeData = coroutineScope {
                    val popularDeferred = async { movieRepository.getPopularMovies() }
                    val topRatedDeferred = async { movieRepository.getTopRatedMovies() }
                    val upcomingDeferred = async { movieRepository.getUpcomingMovies() }
                    val nowPlayingDeferred = async { movieRepository.getNowPlayingMovies() }

                    HomeData(
                        popular = popularDeferred.await(),
                        topRated = topRatedDeferred.await(),
                        upcoming = upcomingDeferred.await(),
                        nowPlaying = nowPlayingDeferred.await()
                    )
                }

                _uiState.value = HomeUi(
                    slides = homeData.nowPlaying.map { it.toSliderUi() },
                    sections = listOf(
                        createSection("Popular", MovieSectionType.POPULAR, homeData.popular),
                        createSection("Top Rated", MovieSectionType.TOP_RATED, homeData.topRated),
                        createSection("Upcoming", MovieSectionType.UPCOMING, homeData.upcoming)
                    )
                )
            } catch (e: Exception) {
                _uiState.value = HomeUi(errorMessage = e.message)
            }
        }
    }

    private fun createSection(
        title: String,
        type: MovieSectionType,
        movies: List<Movie>
    ): MovieSectionUi {
        return MovieSectionUi(
            title = title,
            type = type,
            movies = movies.map { it.toUi() }
        )
    }

    private fun Movie.toUi(): MovieUi {
        return MovieUi(
            id = id,
            rating = voteAverage,
            posterPath = posterPath
        )
    }

    private fun Movie.toSliderUi(): SliderUi {
        return SliderUi(
            imageUrl = backdropPath.orEmpty(),
            title = title
        )
    }
}