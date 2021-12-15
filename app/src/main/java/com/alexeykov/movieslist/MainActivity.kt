package com.alexeykov.movieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.alexeykov.movieslist.data.RecyclerViewUpdater

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.RecyclerView)
        val recyclerViewUpdater = RecyclerViewUpdater(recyclerView)
        recyclerViewUpdater.setEmptyList()
        recyclerViewUpdater.getAllMoviesList()
    }

}