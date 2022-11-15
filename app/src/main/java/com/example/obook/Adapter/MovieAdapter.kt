package com.example.obook.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.R
import com.google.android.gms.common.data.DataHolder
import kotlinx.android.synthetic.main.movie_item.view.*


open class MovieAdapter(private var movies:MutableList<Movies>):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable{
    lateinit var mListener: onItemClickListener
    protected var values = movies
    private var onNothingFound: (() -> Unit)? = null
    interface onItemClickListener{
        fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    fun updateList(list: List<Movies>) {
        movies = list as MutableList<Movies>
        notifyDataSetChanged()
    }
    class MovieViewHolder(itemView:View, private var listener: onItemClickListener):RecyclerView.ViewHolder(itemView){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        var title = ""
        fun bindMovie(movie: Movies){
            itemView.movie_title.text = movie.title
            title = movie.title!!
            Glide.with(itemView).load(IMAGE_BASE + movie.poster_path).into(itemView.poster)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
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