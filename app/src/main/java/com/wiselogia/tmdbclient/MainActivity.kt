package com.wiselogia.tmdbclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiselogia.tmdbclient.databinding.SearcherBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: SearcherBinding
    private var lastAskList = 1
    private var request = ""
    private var isLoading = false
    private val movieAdapter by lazy { MovieAdapter{
        startActivity(Intent(this@MainActivity, MovieActivity::class.java).apply {
            putExtra("id", it.id)
        })
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearcherBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.moviesView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = movieAdapter
        }

        binding.moviesView.setOnScrollChangeListener { view, _, _, _, _ ->
            if(((view as RecyclerView).layoutManager as GridLayoutManager).findLastVisibleItemPosition() <
                (view.adapter as RecyclerView.Adapter).itemCount - 10 && !isLoading) {
                isLoading = true
                getList()
            }
        }
        binding.searchButton.setOnClickListener {
            isLoading = true
            if(binding.value.text.toString() != request) {
                movieAdapter.clear()
                request = binding.value.text.toString()
            }
            getList()

        }
    }
    fun getList() {
        TMDBService.getListData(
            lastAskList++,
            request,
            object : TMDBService.OnResponseListener<MovieList> {
                override fun onSuccess(data: MovieList) {
                    isLoading = false
                    movieAdapter.addItems(data.results)
                }

                override fun onFailed(code: Int) {
                    val toast = Toast.makeText(
                        this@MainActivity,
                        "1error: $code",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            })
    }
}

