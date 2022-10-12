package com.wiselogia.tmdbclient.data

import com.google.gson.annotations.SerializedName

data class SeriesFull(
    @SerializedName("original_name")
    val title: String = "",
    @SerializedName("poster_path")
    val image: String = "",
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    @SerializedName("first_air_date")
    val first_air_date: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("number_of_episodes")
    val episodes: Int = 0,
    @SerializedName("number_of_seasons")
    val seasons: Int = 0
)