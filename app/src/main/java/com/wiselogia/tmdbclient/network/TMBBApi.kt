package com.wiselogia.tmdbclient.network


import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.data.MovieList
import com.wiselogia.tmdbclient.data.SeriesFull
import com.wiselogia.tmdbclient.data.SeriesList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMBBApi {
    @GET("search/movie")
    fun getFilmListDataObservable(
        @Query("api_key") key: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): Observable<MovieList>

    @GET("movie/{movie_id}")
    fun getFullMovieDataObservable(
        @Path(value = "movie_id", encoded = false) id: Int,
        @Query("api_key") key: String
    ): Observable<MovieFull>

    @GET("search/tv")
    fun getSeriesListDataObservable(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Observable<SeriesList>

    @GET("tv/{tv_id}")
    fun getFullSeriesDataObservable(
        @Path(value = "tv_id", encoded = false) id: Int,
        @Query("api_key") key: String
    ): Observable<SeriesFull>
}