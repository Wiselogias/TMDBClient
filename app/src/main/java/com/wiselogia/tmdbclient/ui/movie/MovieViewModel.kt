package com.wiselogia.tmdbclient.ui.movie

import androidx.lifecycle.ViewModel
import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.network.TMDBService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class MovieViewModel(val id: Int) : ViewModel() {
    val movieData: Observable<MovieFull> = TMDBService
        .getMovieDataObservable(id)
        .observeOn(AndroidSchedulers.mainThread())
}