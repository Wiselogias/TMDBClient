package com.wiselogia.tmdbclient.ui.movie

import androidx.lifecycle.ViewModel
import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.network.TMDBService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class MovieViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    val dataSubject: PublishSubject<MovieFull> = PublishSubject.create()
    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create()

    private val networkListSubscriber = object : Observer<MovieFull> {
        override fun onSubscribe(d: Disposable?) {
            disposable.add(d)
        }

        override fun onNext(value: MovieFull?) {
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

    private fun loadData(load: Int): Observable<MovieFull> {
        return TMDBService.getMovieDataObservable(load)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}