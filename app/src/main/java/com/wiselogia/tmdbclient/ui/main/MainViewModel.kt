package com.wiselogia.tmdbclient.ui.main

import androidx.lifecycle.ViewModel
import com.wiselogia.tmdbclient.network.TMDBService
import io.reactivex.Observable
import io.reactivex.Observable.merge
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainViewModel : ViewModel() {
    var page = 1

    val listSubject: PublishSubject<List<ShowableModel>> = PublishSubject.create()
    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create()
    val clearSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

    private val disposable = CompositeDisposable()

    private val networkListSubscriber = object : Observer<List<ShowableModel>> {
        override fun onSubscribe(d: Disposable?) {
            disposable.add(d)
        }

        override fun onNext(value: List<ShowableModel>?) {
            listSubject.onNext(value)
        }

        override fun onError(e: Throwable?) {
            e?.printStackTrace()
            errorSubject.onNext(e)
        }

        override fun onComplete() {}

    }

    fun viewChanges(text: Observable<String>, onPage: Subject<Unit>) {
        disposable.add(
            text.subscribe {
                page = 1
                getShowableInfo(page, it)
                    .subscribe(networkListSubscriber)
                clearSubject.onNext(Unit)
            }
        )
        disposable.add(
            onPage.flatMap { text }.subscribe { query ->
                page++
                getShowableInfo(page, query)
                    .subscribe(networkListSubscriber)
            }
        )
    }

    private fun getShowableInfo(page: Int, query: String): Observable<List<ShowableModel>> = merge(
        TMDBService.getFilmListDataObservable(page, query).map { list ->
            list.map {
                ShowableModel(it.title, it.image ?: "", it.id, ShowableModelTypes.MOVIE)
            }
        },
        TMDBService.getSeriesListDataObservable(page, query).map { list ->
            list.map {
                ShowableModel(it.title, it.image ?: "", it.id, ShowableModelTypes.SERIES)
            }
        }
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}