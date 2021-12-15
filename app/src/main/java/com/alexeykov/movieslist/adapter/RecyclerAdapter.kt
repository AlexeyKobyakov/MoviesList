package com.alexeykov.movieslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexeykov.movieslist.R
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import kotlin.collections.ArrayList

class RecyclerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var arrayList: MutableList<Model> = ArrayList()

    fun submitEmptyItem() {
        val list = ArrayList<Model>()
        val loadingStateItem = LoadingStateItem(
            isLoading = true,
            isRetry = false,
            error_message = ""
        )
        list.add(Model.LoadingModel)
        arrayList.addAll(list)
        notifyItemInserted(0)
    }

    fun deleteLast() {
        arrayList.removeAt(arrayList.size - 1)
        notifyItemRemoved(arrayList.size - 1)
    }

    fun submitNetData(response: String, size: Int) {
        val resultsArray = JSONArray(response)
        val list = ArrayList<Model>()

        for (i in 0 until size - 1) {
            val item = JSONObject(resultsArray.getString(i))
            val multimediaString = item.getString("multimedia")
            val multimedia: JSONObject? = try {
                JSONObject(multimediaString) }
            catch (e: Exception) {
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
        arrayList.removeAt(arrayList.size - 1)
        arrayList.addAll(list)
        submitEmptyItem()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getItemViewType(position: Int) = when (arrayList[position]) {
        is Model.MovieModel -> R.layout.recycler_item
        is Model.LoadingModel -> R.layout.recycler_loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.recycler_item -> MovieViewHolder(v)
            else -> LoadingViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = arrayList[position]
        when (holder) {
            is MovieViewHolder -> holder.onBindView(item as Model.MovieModel)
            is LoadingViewHolder -> holder.onBindView()
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nameTextView: TextView? = null
        private var descriptionTextView: TextView? = null
        private var image: ImageView? = null

        init {
            nameTextView = itemView.findViewById(R.id.movie_name)
            descriptionTextView = itemView.findViewById(R.id.movie_description)
            image = itemView.findViewById(R.id.movie_image)
        }

        fun onBindView(item: Model.MovieModel) {
            nameTextView?.text = item.movieItem.name
            descriptionTextView?.text = item.movieItem.description
            image?.layoutParams?.width = item.movieItem.imageWidth
            image?.layoutParams?.height = item.movieItem.imageHeight
            Picasso.get().load(item.movieItem.imageUrl).placeholder(R.drawable.ic_movies)
                .error(R.drawable.ic_error).into(image)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var progressBar: ProgressBar? = null

        init {
            progressBar = itemView.findViewById(R.id.progress_bar)
        }

        fun onBindView() {
        }
    }
}