package com.azyabon.moviesapp.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.MovieCardBinding
import com.azyabon.moviesapp.databinding.SectionMovieCardBinding
import com.azyabon.moviesapp.presentation.common.MovieCardType
import com.azyabon.moviesapp.presentation.common.TMDB_IMAGE_BASE_URL_780
import com.azyabon.moviesapp.presentation.common.model.MovieUi

class MoviesAdapter(
    private val cardType: MovieCardType,
    private val onItemClick: (movieId: Int) -> Unit
) : ListAdapter<MovieUi, RecyclerView.ViewHolder>(DiffCallback) {

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

    override fun getItemViewType(position: Int): Int {
        return cardType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (MovieCardType.entries[viewType]) {
            MovieCardType.SECTION -> SectionMovieViewHolder(
                SectionMovieCardBinding.inflate(inflater, parent, false)
            )

            MovieCardType.GRID -> GridMovieViewHolder(
                MovieCardBinding.inflate(inflater, parent, false)
            )
        }
    }

    private fun bindMovieCard(
        movie: MovieUi,
        movieCard: CardView,
        posterView: ImageView,
        ratingView: TextView
    ) {
        ratingView.text = "%.1f".format(movie.rating)

        posterView.load(movie.posterPath?.let { TMDB_IMAGE_BASE_URL_780 + it }) {
            crossfade(true)
            placeholder(R.drawable.blank)
        }

        movieCard.setOnClickListener {
            onItemClick(movie.id)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)

        when (holder) {
            is SectionMovieViewHolder -> holder.bind(movie)
            is GridMovieViewHolder -> holder.bind(movie)
        }
    }

    inner class SectionMovieViewHolder(
        private val binding: SectionMovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieUi) {
            bindMovieCard(
                movie = movie,
                movieCard = binding.movieCard,
                posterView = binding.ivMovie,
                ratingView = binding.tvMovieRating
            )
        }
    }

    inner class GridMovieViewHolder(
        private val binding: MovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieUi) {
            bindMovieCard(
                movie = movie,
                movieCard = binding.movieCard,
                posterView = binding.ivMovie,
                ratingView = binding.tvMovieRating
            )
        }
    }
}