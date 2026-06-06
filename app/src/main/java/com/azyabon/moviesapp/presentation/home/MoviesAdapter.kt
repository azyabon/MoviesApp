package com.azyabon.moviesapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azyabon.moviesapp.databinding.SectionMovieCardBinding
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.azyabon.moviesapp.R

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"

class MoviesAdapter(
    private val onItemClick: () -> Unit
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

    override fun onBindViewHolder(holder: MoviesAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.binding.apply {
            tvMovieRating.text = "%.1f".format(movie.rating).toString()
            ivMovie.load(movie.posterPath?.let { IMAGE_BASE_URL + it }) {
                crossfade(true)
                placeholder(R.drawable.blank)
            }

            movieCard.setOnClickListener {
                onItemClick()
            }
        }
    }

    class MovieViewHolder(val binding: SectionMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root)

}