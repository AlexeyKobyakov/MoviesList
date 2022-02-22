package com.alexeykov.movieslist.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexeykov.movieslist.R
import com.alexeykov.movieslist.databinding.RecyclerItemBinding
import com.alexeykov.movieslist.databinding.RecyclerLoadingBinding
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class RecyclerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var arrayList: MutableList<Model> = ArrayList()

    fun setMovieList(movies: List<Model>) {
//        val moviesDiffUtilCallback = MoviesDiffUtilCallback(movies.toMutableList(), arrayList)
//        val moviesDiffResult = DiffUtil.calculateDiff(moviesDiffUtilCallback)
//        moviesDiffResult.dispatchUpdatesTo(this)
        arrayList = movies.toMutableList()
        notifyDataSetChanged()
    }

    fun getData(): MutableList<Model> = arrayList

    override fun getItemCount(): Int = arrayList.size

    override fun getItemViewType(position: Int) = when (arrayList[position]) {
        is Model.MovieModel -> MOVIE_ITEM
        is Model.LoadingModel -> LOADING_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            MOVIE_ITEM -> {
                val binding = RecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieViewHolder(binding)
            }
            else -> {
                val binding = RecyclerLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = arrayList[position]
        Log.d(TAG, "bind, position = $position")
        when (holder) {
            is MovieViewHolder -> holder.onBindView(item as Model.MovieModel)
            is LoadingViewHolder -> holder.onBindView()
        }
    }

    inner class MovieViewHolder(
        private val binding: RecyclerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBindView(item: Model.MovieModel) {
            with(binding) {
                movieName.text = item.movieItem.name
                movieDescription.text = item.movieItem.description
                movieImage.layoutParams?.width = item.movieItem.imageWidth
                movieImage.layoutParams?.height = item.movieItem.imageHeight
                Picasso.get().load(item.movieItem.imageUrl).placeholder(R.drawable.ic_movies)
                    .error(R.drawable.ic_error).into(movieImage)
            }
        }
    }

    inner class LoadingViewHolder(
        binding: RecyclerLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBindView() {
        }
    }

    companion object {
        const val MOVIE_ITEM = 1
        const val LOADING_ITEM = 2
    }
}