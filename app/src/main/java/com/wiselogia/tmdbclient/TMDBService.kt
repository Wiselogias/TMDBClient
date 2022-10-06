package com.wiselogia.tmdbclient

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

    val tmdbApi = retrofit.create(TMBBApi::class.java)


    fun getData(id: Int, listener: OnResponseListener<MovieFull>) {
        tmdbApi.getFullData(id, "b94c9c8d4eb47d5e39df3ede28eca5dd").enqueue(
            object : Callback<MovieFull> {
                override fun onResponse(call: Call<MovieFull>, response: Response<MovieFull>) {
                    when {
                        response.code() == 200 && response.body() != null -> listener.onSuccess(response.body()!!)
                        else -> listener.onFailed(response.code())
                    }
                }

                override fun onFailure(call: Call<MovieFull>, t: Throwable) {
                    listener.onFailed(0)
                }

            }
        )
    }

    fun getListData(page: Int, query: String, listener: OnResponseListener<MovieList>) {
        tmdbApi.getListData("b94c9c8d4eb47d5e39df3ede28eca5dd", page, query).enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                when {
                    response.code() == 200 && response.body() != null -> listener.onSuccess(response.body()!!)
                    else -> listener.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                listener.onFailed(0)
            }

        })
    }


    interface OnResponseListener<T> {
        fun onSuccess(data: T)
        fun onFailed(code: Int)
    }
}