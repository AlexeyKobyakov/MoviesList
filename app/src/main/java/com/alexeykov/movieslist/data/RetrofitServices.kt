package com.alexeykov.movieslist.data

import com.alexeykov.movieslist.BuildConfig
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {

    @GET("all.json?api-key=" + BuildConfig.API_kEY)
    fun getMovieList(
        @Query("offset") nextOffset: String
    ): Call<JsonElement>

}