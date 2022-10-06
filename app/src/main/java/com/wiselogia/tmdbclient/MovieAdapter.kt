package com.wiselogia.tmdbclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val onClick: (Movie) -> Unit):
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    var movies = listOf<Movie>()
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
    inner class MovieHolder(private val root: View) :RecyclerView.ViewHolder(root) {
        private val imageView = root.findViewById<ImageView>(R.id.moviePoster)
        private val titleView = root.findViewById<TextView>(R.id.movieTitle)

        fun bind(movie: Movie, onClick: (Movie) -> Unit) {
                imageView.glide(movie.image ?: "")
            titleView.text = movie.title
            root.setOnClickListener {
                onClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false))
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position], onClick)

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