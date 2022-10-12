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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class MovieActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    lateinit var binding: MovieInfoBinding

    val idSubject: BehaviorSubject<Int> = BehaviorSubject.create()

    private val movieViewModel by lazy {
        ViewModelProvider(
            viewModelStore,
            defaultViewModelProviderFactory
        )[MovieViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        idSubject.onNext(intent.getIntExtra("id", 0))
    }

    override fun onStart() {
        super.onStart()
        setObservers()
        movieViewModel.showData(idSubject)
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        disposable.add(
            movieViewModel.dataSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.overviewView.text = it.overview
                    binding.statusView.text = "Status: " + it?.status
                    binding.budgetView.text = "Budget: " + it?.budget.toString()
                    binding.dateView.text = "Release: " + it?.release_date
                    binding.promoView.glide(it?.image ?: "")
                    binding.popularityView.text = "Popularity: " + it?.popularity.toString()
                    binding.nameView.text = it?.title
                    binding.backgroundView.glideWithBlur(it?.image ?: "")
                }
        )
        disposable.add(
            movieViewModel.errorSubject
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