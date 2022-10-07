package com.wiselogia.tmdbclient

import com.wiselogia.tmdbclient.data.MovieFull
import com.wiselogia.tmdbclient.data.MovieList
import com.wiselogia.tmdbclient.network.TMBBApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TMDBService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tmdbApi = retrofit.create(TMBBApi::class.java)

    fun getData(id: Int, listener: OnResponseListener<MovieFull>) {
        tmdbApi
            .getFullData(id, "b94c9c8d4eb47d5e39df3ede28eca5dd")
            .enqueue(RetrofitCallback<MovieFull> (listener::onSuccess, listener::onFailed))
    }

    fun getListData(page: Int, query: String, listener: OnResponseListener<MovieList>) {
        tmdbApi
            .getListData("b94c9c8d4eb47d5e39df3ede28eca5dd", page, query)
            .enqueue(RetrofitCallback<MovieList> (listener::onSuccess, listener::onFailed))
    }


    interface OnResponseListener<T> {
        fun onSuccess(data: T)
        fun onFailed(throwable: Throwable)
    }
}

class RetrofitCallback<T>(private val onSuccess: (T) -> Unit, private val onFailed: (Throwable) -> Unit): Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        when {
            response.code() == 200 && response.body() != null -> onSuccess(response.body()!!)
            else -> onFailed(RetrofitException(response.code().toString()))
        }

    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailed(t)
    }
}

class RetrofitException(message: String?) : Exception(message)