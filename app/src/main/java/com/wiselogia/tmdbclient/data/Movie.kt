package com.wiselogia.tmdbclient.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("poster_path")
    val image: String? = null,
    @SerializedName("id")
    val id: Int = 0
)

