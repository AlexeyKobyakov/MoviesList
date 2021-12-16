package com.alexeykov.movieslist.data

import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexeykov.movieslist.R
import com.alexeykov.movieslist.adapter.MoviesDiffUtilCallback
import com.alexeykov.movieslist.adapter.RecyclerAdapter
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewUpdater(private val recyclerView: RecyclerView) {

    var layoutManager: LinearLayoutManager = LinearLayoutManager(recyclerView.context)
    private var movieAdapter: RecyclerAdapter
    private var rService: RetrofitServices

    private var hasMore = true
    private var pageCount = 0
    private var loading = true
    private var endListReached = false

    init {
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        movieAdapter = RecyclerAdapter()
        addPagination()
        rService = NyTimesData.retrofitService
    }

    fun setEmptyList() {
        movieAdapter.submitEmptyItem()
        recyclerView.adapter = movieAdapter
    }

    fun getAllMoviesList() {
        if (hasMore) {
            var nextOffset = "0"
            if (pageCount * 20 > 0) {
                nextOffset = (pageCount * 20).toString()
            }
            rService.getMovieList(nextOffset).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Toast.makeText(recyclerView.context, t.message, Toast.LENGTH_LONG).show()
                    movieAdapter.deleteLast()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val element: JsonElement = response.body() as JsonElement
                    val jsonObject = JSONObject(element.toString())
                    if (jsonObject.getString("status").equals("OK")) {
                        hasMore = jsonObject.getBoolean("has_more")

                        val oldData = movieAdapter.getData()

                        movieAdapter.submitNetData(jsonObject.getString("results"),
                            jsonObject.getInt("num_results"))

                        val moviesDiffUtilCallback = MoviesDiffUtilCallback(oldData, movieAdapter.getData())
                        val moviesDiffResult = DiffUtil.calculateDiff(moviesDiffUtilCallback)
                        moviesDiffResult.dispatchUpdatesTo(movieAdapter)
                        loading = false
                    }
                }
            })
        } else {
            if (!endListReached) {
                endListReached = true
                movieAdapter.deleteLast()
            } else {
                Toast.makeText(recyclerView.context, recyclerView.context.getString(R.string.no_more_data), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == movieAdapter.itemCount - 1) {
                    if (!loading) {
                        loading = true
                        ++pageCount
                        getAllMoviesList()
                    }
                }
            }
        })
    }
}