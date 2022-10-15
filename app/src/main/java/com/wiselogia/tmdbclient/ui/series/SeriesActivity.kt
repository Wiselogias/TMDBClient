package com.wiselogia.tmdbclient.ui.series

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wiselogia.tmdbclient.databinding.SeriesInfoBinding
import com.wiselogia.tmdbclient.utilities.glide
import com.wiselogia.tmdbclient.utilities.glideWithBlur
import io.reactivex.disposables.CompositeDisposable
class SeriesActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    lateinit var binding: SeriesInfoBinding
    private lateinit var seriesViewModel: SeriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SeriesInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        seriesViewModel = ViewModelProvider(
            viewModelStore,
            SeriesViewModelFactory(intent.getIntExtra("id", 0))
        )[SeriesViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        disposable.add(
            seriesViewModel.seriesData
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
                    binding.dateView.text = "Release: ${it?.first_air_date}"
                    binding.promoView.glide(it?.image ?: "")
                    binding.popularityView.text = "Popularity: ${it?.popularity}"
                    binding.nameView.text = it?.title
                    binding.seriesView.text = "Episodes: ${it.episodes}"
                    binding.seasonsView.text = "Seasons: ${it.seasons}"
                    binding.backgroundView.glideWithBlur(it?.image ?: "")
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}