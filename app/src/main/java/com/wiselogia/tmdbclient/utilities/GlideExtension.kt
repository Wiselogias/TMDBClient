package com.wiselogia.tmdbclient

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.glide(path : String) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/$path")
        .error(R.drawable.ic_outline_broken_image_24)
        .centerCrop()
        .into(this)

}