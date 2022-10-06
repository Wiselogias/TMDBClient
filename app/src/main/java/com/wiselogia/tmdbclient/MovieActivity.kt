package com.wiselogia.tmdbclient

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MovieActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_info)
        val id = intent.getIntExtra("id", 0)
        TMDBService.getData(id, object : TMDBService.OnResponseListener<MovieFull> {
            override fun onSuccess(data: MovieFull) {
                findViewById<TextView>(R.id.nameView).text = data.title
                findViewById<TextView>(R.id.popularityView).text = data.popularity.toString()
                findViewById<TextView>(R.id.dateView).text = data.release_date
                findViewById<TextView>(R.id.statusView).text = data.status
                findViewById<TextView>(R.id.budgetView).text = data.budget.toString()
                findViewById<ImageView>(R.id.promoView).glide(data.image ?: "")
            }

            override fun onFailed(code: Int) {
                val toast = Toast.makeText(this@MovieActivity, "1error: $code", Toast.LENGTH_SHORT)
                toast.show()
            }

        })
    }
}