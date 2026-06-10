package com.azyabon.moviesapp.presentation.movie

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.FragmentMovieBinding
import com.azyabon.moviesapp.presentation.common.TMDB_IMAGE_BASE_URL_780
import com.azyabon.moviesapp.presentation.common.formatMoney
import com.azyabon.moviesapp.presentation.common.formatReleaseYear
import com.azyabon.moviesapp.presentation.common.formatRuntime
import com.azyabon.moviesapp.presentation.common.getRatingColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private val viewModel: MovieViewModel by viewModels()
    private var _binding: FragmentMovieBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("View binding is only valid between onCreate and onDestroy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBarOffsetListener()
        val genreAdapter = GenresAdapter()

        with(binding) {
            movieToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFavorite.setOnClickListener {
                val text = "Has been added to favorites"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }

            llOverview.setOnClickListener {
                tvOverview.isVisible = !tvOverview.isVisible
            }

            rvGenres.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvGenres.adapter = genreAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val movie = uiState.movie

                    movie?.let { m ->
                        with(binding) {
                            tvTitle.text = m.title
                            tvTagline.text = m.tagline
                            ivMoviePoster.load(m.posterPath?.let { TMDB_IMAGE_BASE_URL_780 + it }) {
                                crossfade(true)
                                placeholder(R.drawable.blank)
                            }

                            (activity as? AppCompatActivity)?.supportActionBar?.title = m.title
                            tvOverview.text = m.overview
                            tvBudget.text = getString(R.string.tv_budget, formatMoney(m.budget))
                            tvRevenue.text = getString(R.string.tv_revenue, formatMoney(m.revenue))
                            tvReleaseDate.text = formatReleaseYear(m.releaseDate)
                            tvRuntime.text = formatRuntime(m.runtime)

                            val color = ContextCompat.getColor(
                                tvVoteAverage.context,
                                getRatingColor(movie.voteAverage)
                            )

                            tvVoteAverage.text = "%.1f".format(movie.voteAverage).toString()
                            tvVoteAverage.setTextColor(color)

                            genreAdapter.submitList(m.genres)
                        }
                    }
                }
            }
        }
    }

    private fun setupAppBarOffsetListener() {
        binding.movieAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapseProgress = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            val isCollapsed = collapseProgress > 0.7f

            binding.vTouch.isVisible = !isCollapsed

            val iconColor = requireContext().getColor(
                if (isCollapsed) R.color.text else R.color.white
            )

            val toolbarBtnWidth = if (isCollapsed) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                resources.getDimensionPixelSize(R.dimen.movie_toolbar_button_width)
            }

            binding.btnBack.apply {
                background = if (isCollapsed) null else ContextCompat.getDrawable(requireContext(), R.drawable.bg_opacity)
                imageTintList = ColorStateList.valueOf(iconColor)

                layoutParams = layoutParams.apply {
                    this.width = toolbarBtnWidth
                }
            }
            binding.btnFavorite.apply {
                background = if (isCollapsed) null else ContextCompat.getDrawable(requireContext(), R.drawable.bg_opacity)
                imageTintList = ColorStateList.valueOf(iconColor)

                layoutParams = layoutParams.apply {
                    this.width = toolbarBtnWidth
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}