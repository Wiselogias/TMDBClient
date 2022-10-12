package com.wiselogia.tmdbclient.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiselogia.tmdbclient.databinding.MovieCardBinding
import com.wiselogia.tmdbclient.ui.main.ShowableModel

class ShowableAdapter(private val onClick: (ShowableModel) -> Unit) :
    RecyclerView.Adapter<ShowableAdapter.ShowableHolder>() {
    private var showables = listOf<ShowableModel>()
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

    inner class ShowableHolder(private val binding: MovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.moviePoster
        private val titleView = binding.movieTitle

        fun bind(showable: ShowableModel, onClick: (ShowableModel) -> Unit) {
            imageView.glide(showable.image)
            titleView.text = showable.title
            binding.root.setOnClickListener {
                onClick(showable)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowableHolder {
        return ShowableHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowableHolder, position: Int) =
        holder.bind(showables[position], onClick)

    override fun getItemCount(): Int = showables.size

    fun addItems(newItems: List<ShowableModel>) {
        showables = showables.toMutableList().apply {
            addAll(newItems)
        }
    }

    fun clear() {
        showables = listOf()
    }

}