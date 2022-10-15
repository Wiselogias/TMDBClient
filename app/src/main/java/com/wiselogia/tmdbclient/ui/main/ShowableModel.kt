package com.wiselogia.tmdbclient.ui.main

data class ShowableModel(
    val title: String = "",
    val image: String = "",
    val id: Int = 0,
    val type: ShowableModelTypes = ShowableModelTypes.MOVIE
)

enum class ShowableModelTypes {
    MOVIE, SERIES
}