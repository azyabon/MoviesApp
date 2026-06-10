package com.azyabon.moviesapp.presentation.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.FragmentHomeBinding
import com.azyabon.moviesapp.databinding.ViewSectionHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private companion object {
        const val SLIDER_DELAY_MS = 5000L
    }

    private var isSliderPositionInitialized = false
    private lateinit var viewPager2: ViewPager2
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
        setUpTransformer()

        startSliderAutoScroll()
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun setupRecyclerViews() {
        val popularAdapter = MoviesAdapter(::onMovieClick)
        val topRatedAdapter = MoviesAdapter(::onMovieClick)
        val upcomingAdapter = MoviesAdapter(::onMovieClick)
        val sliderAdapter = SliderAdapter(::onMovieClick)

        setupHorizontalMoviesList(binding.rvPopularMovies, popularAdapter)
        setupHorizontalMoviesList(binding.rvTopRatedMovies, topRatedAdapter)
        setupHorizontalMoviesList(binding.rvUpcomingMovies, upcomingAdapter)

        viewPager2 = binding.vpMovies
        viewPager2.apply {
            adapter = sliderAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        observeUiState(popularAdapter, topRatedAdapter, upcomingAdapter, sliderAdapter)
    }

    private fun startSliderAutoScroll() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (isActive) {
                    delay(SLIDER_DELAY_MS)

                    if ((viewPager2.adapter?.itemCount ?: 0) == 0) continue

                    viewPager2.currentItem = viewPager2.currentItem + 1
                }
            }
        }
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
        upcomingAdapter: MoviesAdapter,
        sliderAdapter: SliderAdapter,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    renderSection(
                        section = uiState.getSection(MovieSectionType.POPULAR),
                        headerBinding = binding.popularHeader,
                        adapter = popularAdapter,
                        isLoading = uiState.isLoading,
                    )

                    renderSection(
                        section = uiState.getSection(MovieSectionType.TOP_RATED),
                        headerBinding = binding.topRatedHeader,
                        adapter = topRatedAdapter,
                        isLoading = uiState.isLoading,
                    )

                    renderSection(
                        section = uiState.getSection(MovieSectionType.UPCOMING),
                        headerBinding = binding.upcomingHeader,
                        adapter = upcomingAdapter,
                        isLoading = uiState.isLoading,
                    )

                    sliderAdapter.submitList(uiState.slides)

                    if (!isSliderPositionInitialized && uiState.slides.isNotEmpty()) {
                        val startPosition = Int.MAX_VALUE / 2
                        val correctedPosition =
                            startPosition - startPosition % uiState.slides.size

                        binding.vpMovies.setCurrentItem(correctedPosition, false)

                        isSliderPositionInitialized = true
                    }

                    binding.liLoader.isVisible = uiState.isLoading
                }
            }
        }
    }

    private fun renderSection(
        section: MovieSectionUi?,
        headerBinding: ViewSectionHeaderBinding,
        adapter: MoviesAdapter,
        isLoading: Boolean,
    ) {
        headerBinding.root.isVisible = !isLoading
        headerBinding.tvSectionTitle.text = section?.title.orEmpty()
        headerBinding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_moviesFragment)
        }

        adapter.submitList(section?.movies.orEmpty())
    }

    private fun onMovieClick(movieId: Int) {
        val bundle = bundleOf("movieId" to movieId)
        findNavController().navigate(R.id.action_homeFragment_to_movieFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isSliderPositionInitialized = false
        _binding = null
    }

    private fun HomeUi.getSection(type: MovieSectionType): MovieSectionUi? {
        return sections.firstOrNull { it.type == type }
    }
}