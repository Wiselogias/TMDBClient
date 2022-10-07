package com.wiselogia.tmdbclient.network

import com.wiselogia.tmdbclient.data.Movie
import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.data.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMBBApi {
    @GET("movie/{movie_id}")
    fun getData(@Path(value = "movie_id", encoded = false) id: Int, @Query("api_key") key: String) : Call<Movie>
    @GET("movie/{movie_id}")
    fun getFullData(@Path(value = "movie_id", encoded = false) id: Int, @Query("api_key") key: String) : Call<MovieFull>
    @GET("search/movie")
    fun getListData(@Query("api_key") key: String, @Query("page") page: Int, @Query("query") query: String) : Call<MovieList>
}