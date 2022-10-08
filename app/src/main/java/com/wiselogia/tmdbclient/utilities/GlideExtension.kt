package com.wiselogia.tmdbclient.utilities

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wiselogia.tmdbclient.R
import jp.wasabeef.glide.transformations.BlurTransformation

fun ImageView.glide(path : String) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/$path")
        .error(R.drawable.ic_outline_broken_image_24)
        .centerCrop()
        .into(this)

}

fun ImageView.glideWithBlur(path : String) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/$path")
        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
        .error(R.drawable.ic_outline_broken_image_24)
        .centerCrop()
        .into(this)

}