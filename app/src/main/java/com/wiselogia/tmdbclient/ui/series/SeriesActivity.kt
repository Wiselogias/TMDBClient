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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class SeriesActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    lateinit var binding: SeriesInfoBinding
    private val idSubject = BehaviorSubject.create<Int>()
    private val seriesViewModel by lazy {
        ViewModelProvider(
            viewModelStore,
            defaultViewModelProviderFactory
        )[SeriesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SeriesInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        idSubject.onNext(intent.getIntExtra("id", 0))
    }

    override fun onStart() {
        super.onStart()
        setObservers()
        seriesViewModel.showData(idSubject)
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        disposable.add(
            seriesViewModel.dataSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.overviewView.text = it.overview
                    binding.statusView.text = "Status: " + it?.status
                    binding.dateView.text = "Release: " + it?.first_air_date
                    binding.promoView.glide(it?.image ?: "")
                    binding.popularityView.text = "Popularity: " + it?.popularity.toString()
                    binding.nameView.text = it?.title
                    binding.seriesView.text = "Episodes: " + it.episodes.toString()
                    binding.seasonsView.text = "Seasons: " + it.seasons.toString()
                    binding.backgroundView.glideWithBlur(it?.image ?: "")
                }
        )

        disposable.add(
            seriesViewModel.errorSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.message != "HTTP 422 ") {
                        Toast.makeText(
                            this,
                            "error with code: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}