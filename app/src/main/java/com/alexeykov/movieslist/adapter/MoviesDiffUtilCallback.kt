package com.alexeykov.movieslist.adapter

import androidx.recyclerview.widget.DiffUtil

class MoviesDiffUtilCallback(
    private val oldList: MutableList<Model>,
    private val newList: MutableList<Model>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel: Model = oldList[oldItemPosition]
        val newModel: Model = newList[newItemPosition]
        return oldModel == newModel
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: Model = oldList[oldItemPosition]
        val newProduct: Model = newList[newItemPosition]

        when (oldList[oldItemPosition]) {
            is Model.MovieModel -> {
                if (newList[newItemPosition] is Model.MovieModel) {
                        val oldItem: Model.MovieModel = oldProduct as Model.MovieModel
                        val newItem: Model.MovieModel = newProduct as Model.MovieModel

                        return oldItem.movieItem.name == newItem.movieItem.name
                                && oldItem.movieItem.description == newItem.movieItem.description
                                && oldItem.movieItem.imageUrl == newItem.movieItem.imageUrl
                    }
                }
            is Model.LoadingModel -> {
                if (newList[newItemPosition] is Model.LoadingModel) {
                    return true
                }
            }
        }
        return false
    }

}