package com.wiselogia.tmdbclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.wiselogia.tmdbclient.data.Movie
import com.wiselogia.tmdbclient.databinding.ActivityMainBinding
import com.wiselogia.tmdbclient.network.TMDBService
import com.wiselogia.tmdbclient.utilities.LoadMoreListener
import com.wiselogia.tmdbclient.utilities.MovieAdapter
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private val logger = Logger.getLogger(this::class.java.simpleName)

    private val disposable = CompositeDisposable()
    private lateinit var binding: ActivityMainBinding
    private var lastAskList = 1
    private var request = ""
    private lateinit var onLoadMore: LoadMoreListener
    private val movieAdapter by lazy {
        MovieAdapter {
            startActivity(Intent(this@MainActivity, MovieActivity::class.java).apply {
                putExtra("id", it.id)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.moviesView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = movieAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onLoadMore.isLoading = true
                if (query != request) {
                    movieAdapter.clear()
                    request = query ?: ""
                }
                getList()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        onLoadMore = LoadMoreListener {
            getList()
        }
        binding.moviesView.setOnScrollChangeListener(onLoadMore)

    }

    fun getList() {
        TMDBService.getListDataObservable(lastAskList++, request).subscribe(object :
            Observer<List<Movie>> {
            override fun onSubscribe(d: Disposable?) {
                disposable.add(d)
            }

            override fun onNext(value: List<Movie>?) {
                movieAdapter.addItems(value ?: listOf())
            }

            override fun onError(e: Throwable?) {
                logger.info("ERROR")
                logger.info("E: " + e.toString())
                this@MainActivity.runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "error: " + (e?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onComplete() {
                onLoadMore.isLoading = false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}

