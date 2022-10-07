package com.wiselogia.tmdbclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiselogia.tmdbclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastAskList = 1
    private var request = ""
    private lateinit var onLoadMore: LoadMoreListener
    private val movieAdapter by lazy { MovieAdapter{
        startActivity(Intent(this@MainActivity, MovieActivity::class.java).apply {
            putExtra("id", it.id)
        })
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.moviesView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = movieAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                onLoadMore.isLoading = true
                if(query != request) {
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

        onLoadMore = LoadMoreListener{
            getList()
        }
        binding.moviesView.setOnScrollChangeListener(onLoadMore)

    }
    fun getList() {
        TMDBService.getListData(
            lastAskList++,
            request,
            object : TMDBService.OnResponseListener<MovieList> {
                override fun onSuccess(data: MovieList) {
                    onLoadMore.isLoading = false
                    movieAdapter.addItems(data.results)
                }

                override fun onFailed(throwable: Throwable) {
                    val toast = Toast.makeText(
                        this@MainActivity,
                        "1error: " + throwable.message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            })
    }

    inner class LoadMoreListener(private val onLoadMore: () -> Unit) : View.OnScrollChangeListener {
        var isLoading = false
        override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
            if(((p0 as RecyclerView).layoutManager as GridLayoutManager).findLastVisibleItemPosition() <
                (p0.adapter as RecyclerView.Adapter).itemCount - 10 && !isLoading) {
                onLoadMore
                isLoading = true
            }
        }

    }
}

