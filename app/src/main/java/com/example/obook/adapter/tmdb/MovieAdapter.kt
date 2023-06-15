package com.example.obook.adapter.tmdb

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*


open class MovieAdapter(private var movies:MutableList<Movies>):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable{
    lateinit var mListener: OnItemClickListener
    protected var values = movies
    private var onNothingFound: (() -> Unit)? = null
    interface OnItemClickListener{
        fun onItemClick(position: Int, imageView: CardView);
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun addData(newData: List<Movies>) {
        movies.addAll(newData)
        notifyDataSetChanged()
    }
    class MovieViewHolder(itemView:View, private var listener: OnItemClickListener):RecyclerView.ViewHolder(itemView){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
        private val imageView: ImageView = itemView.findViewById(R.id.poster)
        private val cardImageView: CardView = itemView.findViewById(R.id.cardview)
        var title = ""
        fun bindMovie(movie: Movies){
            itemView.movie_title.text = movie.title
            title = movie.title!!
            if(movie.poster_path == null) {
                Glide.with(itemView).load(R.drawable.profile).into(imageView)
            }
            else {
                Picasso.get().load(IMAGE_BASE + movie.poster_path).into(imageView)
                Picasso.get().load(IMAGE_BASE + movie.poster_path).into(object: com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?, from: Picasso.LoadedFrom?) {
                        Palette.from(bitmap!!).generate { palette ->
                            val defaultColor = 0x000000
                            val color = palette?.getDominantColor(defaultColor) ?: defaultColor
                            itemView.ambientBg.setBackgroundColor(color)
                            itemView.cardview.setCardBackgroundColor(color)
                        }
                    }
                    override fun onBitmapFailed(e: Exception?, errorDrawable: android.graphics.drawable.Drawable?) {
                        Log.d("TAG", "bindMovie: ${e?.message}")
                    }
                    override fun onPrepareLoad(placeHolderDrawable: android.graphics.drawable.Drawable?) {
                    }
                })
            }
            itemView.alpha = 0F;
            itemView.animate().alpha(1F).start();
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, imageView = cardImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false), mListener
        )
    }
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    values = movies
                } else {
                    values = ArrayList()
                    val searchResults = movies.filter { searchCriteria(charSearch, it) }
                    values.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = values
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no need to use "results" filtered list provided by this method.
                values = results?.values as ArrayList<Movies>
                notifyDataSetChanged()
            }
        }
    }
    protected open fun searchCriteria(searchText: String, value: Movies) : Boolean {
        return true
    }

    fun search(s: String?, onNothingFound: (() -> Unit)? = null ) {
        this.onNothingFound = onNothingFound
        filter.filter(s)
    }
}