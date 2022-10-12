package com.wiselogia.tmdbclient.ui.series

import androidx.lifecycle.ViewModel
import com.wiselogia.tmdbclient.data.SeriesFull
import com.wiselogia.tmdbclient.network.TMDBService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SeriesViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    val dataSubject: PublishSubject<SeriesFull> = PublishSubject.create()
    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create()

    private val networkListSubscriber = object : Observer<SeriesFull> {
        override fun onSubscribe(d: Disposable?) {
            disposable.add(d)
        }

        override fun onNext(value: SeriesFull?) {
            dataSubject.onNext(value)
        }

        override fun onError(e: Throwable?) {
            errorSubject.onNext(e)
        }

        override fun onComplete() {}

    }

    fun showData(id: Observable<Int>) {
        disposable.add(
            id.subscribe {
                loadData(it).subscribe(networkListSubscriber)
            }
        )
    }

    private fun loadData(load: Int): Observable<SeriesFull> {
        return TMDBService.getSeriesDataObservable(load)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}