package com.wiselogia.tmdbclient

import com.google.gson.annotations.SerializedName

data class MovieFull(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("poster_path")
    val image: String = "",
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    @SerializedName("release_date")
    val release_date: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("budget")
    val budget: Int = 0,
    @SerializedName("overview")
    val overview: String = ""
)
