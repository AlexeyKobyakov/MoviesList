package com.alexeykov.movieslist.adapter

sealed class Model {

    class MovieModel(val movieItem: MovieItem) : Model()

    object LoadingModel : Model()

}

data class MovieItem(
    var name: String,
    var description: String,
    var imageUrl: String,
    var imageHeight: Int,
    var imageWidth: Int
)

/* data class LoadingStateItem(
    var isLoading: Boolean,
    var isRetry: Boolean,
    var error_message: String
) */