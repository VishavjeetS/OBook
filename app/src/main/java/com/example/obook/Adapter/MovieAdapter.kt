package com.example.obook.Adapter

import android.content.Intent
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.R
import com.example.obook.Model.Movies
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(private val movies:List<Movies>):RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){
    lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class MovieViewHolder(itemView:View, private var listener: onItemClickListener):RecyclerView.ViewHolder(itemView){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        fun bindMovie(movie:Movies){
            itemView.movie_title.text = movie.title
//            itemView.release_date.text = movie.release_date
//            itemView.vote_count.text = movie.vote_count
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
}