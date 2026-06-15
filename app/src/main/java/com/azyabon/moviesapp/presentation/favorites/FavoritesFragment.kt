package com.azyabon.moviesapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.FragmentFavoritesBinding
import com.azyabon.moviesapp.presentation.common.MovieCardType
import com.azyabon.moviesapp.presentation.common.adapter.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("View binding is only valid between onCreate and onDestroy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritesAdapter = MoviesAdapter(MovieCardType.GRID, ::onMovieClick)

        with(binding) {
            rvFavoritesMovies.layoutManager = GridLayoutManager(requireContext(), 2)
            rvFavoritesMovies.adapter = favoritesAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { uiState ->

                        favoritesAdapter.submitList(uiState.movies)
                        rvFavoritesMovies.isVisible = uiState.movies.isNotEmpty()
                        tvNoFavorites.isVisible = uiState.movies.isEmpty()
                    }
                }
            }
        }
    }

    private fun onMovieClick(movieId: Int) {
        val bundle = bundleOf("movieId" to movieId)
        findNavController().navigate(R.id.action_favoritesFragment_to_movieFragment, bundle)
    }
}