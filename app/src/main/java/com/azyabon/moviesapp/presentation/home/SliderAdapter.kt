package com.azyabon.moviesapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.azyabon.moviesapp.R
import com.azyabon.moviesapp.databinding.SliderCardBinding
import com.azyabon.moviesapp.presentation.common.TMDB_IMAGE_BASE_URL

class SliderAdapter(
    private val onItemClick: () -> Unit
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var images: List<SliderUi> = emptyList()

    fun submitList(newImages: List<SliderUi>) {
        if (images == newImages) return
        images = newImages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (images.isEmpty()) 0 else Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = images[position % images.size]

        holder.binding.apply {
            tvSliderTitle.text = item.title
            ivSliderImage.load(TMDB_IMAGE_BASE_URL + item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.blank)
            }

            sliderCard.setOnClickListener {
                onItemClick()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderViewHolder {
        return SliderViewHolder(
            SliderCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class SliderViewHolder(val binding: SliderCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}