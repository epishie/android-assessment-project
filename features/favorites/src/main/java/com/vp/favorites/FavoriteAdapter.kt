package com.vp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.databinding.ItemListBinding
import com.vp.favorites.model.FavoriteMovie

class FavoriteAdapter(
    private val onClickMovie: (String) -> Unit
) : ListAdapter<FavoriteMovie, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            onClickMovie(getItem(it).id)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemListBinding,
        onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }

        fun bind(favoriteMovie: FavoriteMovie) {
            Glide.with(binding.poster)
                .load(favoriteMovie.poster)
                .into(binding.poster)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteMovie>() {
            override fun areItemsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FavoriteMovie,
                newItem: FavoriteMovie,
            ): Boolean = oldItem == newItem
        }
    }
}
