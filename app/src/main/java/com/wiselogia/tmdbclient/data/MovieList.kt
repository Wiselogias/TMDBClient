package com.wiselogia.tmdbclient.data

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("results")
    val results: List<Movie> = listOf(),
)
