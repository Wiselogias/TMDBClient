package com.wiselogia.tmdbclient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wiselogia.tmdbclient.databinding.MovieInfoBinding

class MovieActivity: AppCompatActivity() {
    lateinit var binding: MovieInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        TMDBService.getData(id, object : TMDBService.OnResponseListener<MovieFull> {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(data: MovieFull) {
                binding.nameView.text = data.title
                binding.popularityView.text = "Popularity: " + data.popularity.toString()
                binding.dateView.text = "Release date: " + data.release_date
                binding.statusView.text = "Status: " + data.status
                binding.budgetView.text = "Budget: " + data.budget.toString()
                binding.promoView.glide(data.image)
                binding.overviewView.text = data.overview
            }

            override fun onFailed(throwable: Throwable) {
                val toast = Toast.makeText(this@MovieActivity, "error: " + throwable.message, Toast.LENGTH_SHORT)
                toast.show()
            }

        })
    }
}