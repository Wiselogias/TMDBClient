package com.wiselogia.tmdbclient.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wiselogia.tmdbclient.databinding.MovieInfoBinding
import com.wiselogia.tmdbclient.utilities.glide
import com.wiselogia.tmdbclient.utilities.glideWithBlur
import io.reactivex.disposables.CompositeDisposable

class MovieActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    lateinit var binding: MovieInfoBinding

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        movieViewModel = ViewModelProvider(
            viewModelStore,
            MovieViewModelFactory(intent.getIntExtra("id", 0))
        )[MovieViewModel::class.java]

    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        disposable.add(
            movieViewModel.movieData
                .doOnError {
                    if (it.message != "HTTP 422 ") {
                        Toast.makeText(
                            this,
                            "error with code: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .subscribe {
                    binding.overviewView.text = it.overview
                    binding.statusView.text = "Status: ${it?.status}"
                    binding.budgetView.text = "Budget: ${it?.budget}"
                    binding.dateView.text = "Release: ${it?.release_date}"
                    binding.promoView.glide(it?.image ?: "")
                    binding.popularityView.text = "Popularity: ${it?.popularity}"
                    binding.nameView.text = it?.title
                    binding.backgroundView.glideWithBlur(it?.image ?: "")
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}