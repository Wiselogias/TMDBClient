package com.wiselogia.tmdbclient.ui

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.wiselogia.tmdbclient.R
import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.network.TMDBService
import com.wiselogia.tmdbclient.databinding.MovieInfoBinding
import com.wiselogia.tmdbclient.utilities.glide
import com.wiselogia.tmdbclient.utilities.glideWithBlur
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.wasabeef.glide.transformations.BlurTransformation

class MovieActivity: AppCompatActivity() {
    private val disposable = CompositeDisposable()
    lateinit var binding: MovieInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        TMDBService.getDataObservable(id).subscribe(object : Observer<MovieFull> {
            override fun onSubscribe(d: Disposable?) {
                disposable.add(d)
            }

            @SuppressLint("SetTextI18n")
            override fun onNext(value: MovieFull?) {
                binding.overviewView.text = value?.overview
                binding.statusView.text = "Status: " + value?.status
                binding.budgetView.text = "Budget: " + value?.budget.toString()
                binding.dateView.text = "Release: " + value?.release_date
                binding.promoView.glide(value?.image ?: "")
                binding.popularityView.text = "Popularity: " + value?.popularity.toString()
                binding.nameView.text = value?.title
                binding.backgroundView.glideWithBlur(value?.image ?: "")
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                Toast.makeText(
                    this@MovieActivity,
                    "error: " + (e?.message ?: ""),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onComplete() {}

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}