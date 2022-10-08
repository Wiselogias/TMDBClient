package com.wiselogia.tmdbclient.network


import com.wiselogia.tmdbclient.data.Movie
import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.data.MovieList
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMBBApi {
    @GET("search/movie")
    fun getListDataObservable(
        @Query("api_key") key: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ) : Observable<MovieList>

    @GET("movie/{movie_id}")
    fun getFullDataObservable(@Path(value = "movie_id", encoded = false) id: Int,
                              @Query("api_key") key: String) : Observable<MovieFull>
}