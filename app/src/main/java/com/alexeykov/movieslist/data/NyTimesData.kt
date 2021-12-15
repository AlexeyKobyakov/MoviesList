package com.alexeykov.movieslist.data

object NyTimesData {
    private const val url = "https://api.nytimes.com/svc/movies/v2/reviews/"

    val retrofitService: RetrofitServices
        get() = RetrofitClient.loadData(url).create(RetrofitServices::class.java)
}