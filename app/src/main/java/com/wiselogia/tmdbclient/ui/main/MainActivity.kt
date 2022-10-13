package com.wiselogia.tmdbclient.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wiselogia.tmdbclient.databinding.ActivityMainBinding
import com.wiselogia.tmdbclient.ui.movie.MovieActivity
import com.wiselogia.tmdbclient.ui.series.SeriesActivity
import com.wiselogia.tmdbclient.utilities.LoadMoreListener
import com.wiselogia.tmdbclient.utilities.ShowableAdapter
import com.wiselogia.tmdbclient.utilities.onText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MainActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding
    private val scrollSubject = PublishSubject.create<Unit>()
    private var onLoadMore = LoadMoreListener {
        scrollSubject.onNext(Unit)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            viewModelStore,
            defaultViewModelProviderFactory
        )[MainViewModel::class.java]
    }
    private val showableAdapter by lazy {
        ShowableAdapter {
            if (it.type == ShowableModelTypes.MOVIE) {
                startActivity(Intent(this@MainActivity, MovieActivity::class.java).apply {
                    putExtra("id", it.id)
                })
            } else {
                startActivity(Intent(this@MainActivity, SeriesActivity::class.java).apply {
                    putExtra("id", it.id)
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moviesView.apply {
            adapter = showableAdapter
            setOnScrollChangeListener(onLoadMore)
        }
    }

    override fun onStart() {
        super.onStart()
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        viewModel.viewChanges(
            binding.searchView.onText(),
            scrollSubject
        )
    }

    private fun setObservers() {
        disposable.add(
            viewModel.errorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.message != "HTTP 422 ") {
                    Toast.makeText(
                        this,
                        "error with code: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        disposable.add(
            viewModel.listSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
                showableAdapter.addItems(it)
                onLoadMore.isLoading = false
            }
        )
        disposable.add(
            viewModel.clearSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
                showableAdapter.clear()
            }
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

}

