package com.alexeykov.movieslist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexeykov.movieslist.MoviesConstants.TAG
import com.alexeykov.movieslist.R
import com.alexeykov.movieslist.adapter.Model
import com.alexeykov.movieslist.adapter.MovieItem
import com.alexeykov.movieslist.data.NyTimesData
import com.alexeykov.movieslist.data.RetrofitServices
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainViewModel : ViewModel() {

    private var moviesList: MutableList<Model> = ArrayList()

    private val _observableList = MutableLiveData<List<Model>>()
    val observableList: LiveData<List<Model>> = _observableList

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _errorLoadingMessage = MutableLiveData<String>()
    val errorLoadingMessage: LiveData<String> = _errorLoadingMessage

    private var rService: RetrofitServices = NyTimesData.retrofitService

    private var hasMore = true
    private var pageCount = 0
    private var loading = true
    private var endListReached = false

    init {
        Log.d(TAG, "$ACTIVITY_NAME ViewModel created")
        setEmptyList()
        getAllMoviesList()
    }

    private fun setEmptyList() {
        moviesList.add(Model.LoadingModel)
        _observableList.value = moviesList
    }

    private fun getAllMoviesList() {
        if (hasMore) {
            var nextOffset = "0"
            if (pageCount * 20 > 0) {
                nextOffset = (pageCount * 20).toString()
            }
            rService.getMovieList(nextOffset).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    _errorLoadingMessage.postValue(t.message)
                    moviesList.removeAt(moviesList.size - 1)
                    _observableList.value.apply { moviesList }
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val element: JsonElement = response.body() as JsonElement
                    val jsonObject = JSONObject(element.toString())
                    if (jsonObject.getString("status").equals("OK")) {
                        hasMore = jsonObject.getBoolean("has_more")

                        submitNetData(jsonObject.getString("results"),
                            jsonObject.getInt("num_results"))

                        loading = false
                    }
                }
            })
        } else {
            if (!endListReached) {
                endListReached = true
                moviesList.removeAt(moviesList.size - 1)
                _observableList.value = moviesList
            } else {
                _errorMessage.postValue(R.string.no_more_data)
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "$ACTIVITY_NAME ViewModel cleared")
        super.onCleared()
    }

    fun getNextData() {
        if (!loading) {
            loading = true
            ++pageCount
            getAllMoviesList()
        }
    }

    fun submitNetData(response: String, size: Int) {
        val resultsArray = JSONArray(response)
        val list = ArrayList<Model>()

        for (i in 0 until size - 1) {
            val item = JSONObject(resultsArray.getString(i))
            val multimediaString = item.getString("multimedia")
            val multimedia: JSONObject? = try {
                JSONObject(multimediaString)
            } catch (e: Exception) {
                null
            }
            if (multimedia != null) {
                val movieItem = MovieItem(
                    name = item.getString("display_title"),
                    description = item.getString("summary_short"),
                    imageHeight = multimedia.getInt("height"),
                    imageWidth = multimedia.getInt("width"),
                    imageUrl = multimedia.getString("src")
                )
                list.add(Model.MovieModel(movieItem))
            }
        }
        moviesList.removeAt(moviesList.size - 1)
        moviesList.addAll(list)
        moviesList.add(Model.LoadingModel)
        _observableList.value = moviesList
    }

    companion object {
        const val ACTIVITY_NAME: String = "MainViewModel"
    }

}