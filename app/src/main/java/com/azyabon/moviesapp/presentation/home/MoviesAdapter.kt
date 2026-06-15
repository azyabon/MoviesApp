package com.azyabon.moviesapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azyabon.moviesapp.databinding.SectionMovieCardBinding
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.presentation.common.TMDB_IMAGE_BASE_URL_780
import com.azyabon.moviesapp.presentation.common.getRatingColor
import com.azyabon.moviesapp.presentation.common.model.MovieUi

class MoviesAdapter(
    private val onItemClick: (movieId: Int) -> Unit
) : ListAdapter<MovieUi, MoviesAdapter.MovieViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MovieUi>() {
            override fun areItemsTheSame(oldItem: MovieUi, newItem: MovieUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieUi, newItem: MovieUi): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        return MovieViewHolder(
            SectionMovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.binding.apply {
            val color = ContextCompat.getColor(
                tvMovieRating.context,
                getRatingColor(movie.rating)
            )

            tvMovieRating.text = "%.1f".format(movie.rating).toString()
            tvMovieRating.setTextColor(color)
            ivMovie.load(movie.posterPath?.let { TMDB_IMAGE_BASE_URL_780 + it }) {
                crossfade(true)
                placeholder(R.drawable.blank)
            }

            movieCard.setOnClickListener {
                onItemClick(movie.id)
            }
        }
    }

    class MovieViewHolder(val binding: SectionMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}