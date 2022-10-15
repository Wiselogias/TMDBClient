package com.wiselogia.tmdbclient.ui.series

import androidx.lifecycle.ViewModel
import com.wiselogia.tmdbclient.data.SeriesFull
import com.wiselogia.tmdbclient.network.TMDBService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class SeriesViewModel(id: Int) : ViewModel() {
    val seriesData: Observable<SeriesFull> = TMDBService
        .getSeriesDataObservable(id)
        .observeOn(AndroidSchedulers.mainThread())
}