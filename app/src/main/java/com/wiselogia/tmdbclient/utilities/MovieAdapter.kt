package com.wiselogia.tmdbclient.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiselogia.tmdbclient.data.Movie
import com.wiselogia.tmdbclient.databinding.MovieCardBinding

class MovieAdapter(private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    private var movies = listOf<Movie>()
        set(value) {
            val res = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = field.size

                override fun getNewListSize() = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    field[oldItemPosition] == value[newItemPosition]

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) = field[oldItemPosition] == value[newItemPosition]

            })
            field = value
            res.dispatchUpdatesTo(this)
        }

    inner class MovieHolder(private val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.moviePoster
        private val titleView = binding.movieTitle

        fun bind(movie: Movie, onClick: (Movie) -> Unit) {
            imageView.glide(movie.image ?: "")
            titleView.text = movie.title
            binding.root.setOnClickListener {
                onClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) =
        holder.bind(movies[position], onClick)

    override fun getItemCount(): Int = movies.size

    fun addItems(newItems: List<Movie>) {
        movies = movies.toMutableList().apply {
            addAll(newItems)
        }
    }

    fun clear() {
        movies = listOf()
    }

}