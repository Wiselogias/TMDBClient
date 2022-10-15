package com.wiselogia.tmdbclient.data

import com.google.gson.annotations.SerializedName

data class SeriesShort(
    @SerializedName("backdrop_path")
    val image: String? = "",
    @SerializedName("original_name")
    val title: String = "",
    @SerializedName("id")
    val id: Int = 0
)