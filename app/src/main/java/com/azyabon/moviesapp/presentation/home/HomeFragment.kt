package com.azyabon.moviesapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.FragmentHomeBinding
import com.azyabon.moviesapp.databinding.ViewSectionHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("View binding is only valid between onCreate and onDestroy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        val popularAdapter = MoviesAdapter(
            onItemClick = ::onMovieClick
        )
        val topRatedAdapter = MoviesAdapter(
            onItemClick = ::onMovieClick
        )
        val upcomingAdapter = MoviesAdapter(
            onItemClick = ::onMovieClick
        )

        setupHorizontalMoviesList(binding.rvPopularMovies, popularAdapter)
        setupHorizontalMoviesList(binding.rvTopRatedMovies, topRatedAdapter)
        setupHorizontalMoviesList(binding.rvUpcomingMovies, upcomingAdapter)

        observeUiState(popularAdapter, topRatedAdapter, upcomingAdapter)
    }

    private fun setupHorizontalMoviesList(
        recyclerView: RecyclerView,
        moviesAdapter: MoviesAdapter
    ) {
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = moviesAdapter
    }

    private fun observeUiState(
        popularAdapter: MoviesAdapter,
        topRatedAdapter: MoviesAdapter,
        upcomingAdapter: MoviesAdapter
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    renderSection(
                        section = uiState.getSection(MovieSectionType.POPULAR),
                        headerBinding = binding.popularHeader,
                        adapter = popularAdapter
                    )

                    renderSection(
                        section = uiState.getSection(MovieSectionType.TOP_RATED),
                        headerBinding = binding.topRatedHeader,
                        adapter = topRatedAdapter
                    )

                    renderSection(
                        section = uiState.getSection(MovieSectionType.UPCOMING),
                        headerBinding = binding.upcomingHeader,
                        adapter = upcomingAdapter
                    )
                }
            }
        }
    }

    private fun renderSection(
        section: MovieSectionUi?,
        headerBinding: ViewSectionHeaderBinding,
        adapter: MoviesAdapter
    ) {
        headerBinding.tvSectionTitle.text = section?.title.orEmpty()
        headerBinding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_moviesFragment)
        }
        adapter.submitList(section?.movies.orEmpty())
    }

    private fun onMovieClick(): Unit {
        findNavController().navigate(R.id.action_homeFragment_to_movieFragment)
    }

    private fun HomeUi.getSection(type: MovieSectionType): MovieSectionUi? {
        return sections.firstOrNull { it.type == type }
    }
}