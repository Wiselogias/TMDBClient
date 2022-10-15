package com.wiselogia.tmdbclient.utilities

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wiselogia.tmdbclient.R
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.concurrent.TimeUnit

fun ImageView.glide(path: String) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/$path")
        .error(R.drawable.ic_outline_broken_image_24)
        .centerCrop()
        .into(this)

}

fun ImageView.glideWithBlur(path: String) {
    Glide.with(this)
        .load("https://image.tmdb.org/t/p/w500/$path")
        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
        .error(R.drawable.ic_outline_broken_image_24)
        .centerCrop()
        .into(this)

}

fun SearchView.onText(): Observable<String> = BehaviorSubject.create<String>().also {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            if (p0 != "")
                it.onNext(p0)
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            if (p0 != "")
                it.onNext(p0)
            return true
        }
    })

    it.doOnComplete { setOnQueryTextListener(null) }
}.debounce(300, TimeUnit.MILLISECONDS).distinctUntilChanged()
