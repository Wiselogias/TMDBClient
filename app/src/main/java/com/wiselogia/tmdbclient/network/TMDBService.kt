package com.wiselogia.tmdbclient.network

import com.wiselogia.tmdbclient.data.Movie
import com.wiselogia.tmdbclient.data.MovieFull
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

object TMDBService {
    const val APIKEY = "b94c9c8d4eb47d5e39df3ede28eca5dd"
    val scheduler = Schedulers.from(Executors.newFixedThreadPool(10))
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tmdbApi = retrofit.create(TMBBApi::class.java)

    fun getDataObservable(id: Int) : Observable<MovieFull> {
        return tmdbApi
            .getFullDataObservable(id, APIKEY)
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getListDataObservable(
        page: Int,
        query: String
    ): Observable<List<Movie>> {
        return tmdbApi.getListDataObservable(APIKEY, page, query).map {
            it.results
        }.subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }

}