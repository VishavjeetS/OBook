package com.example.obook.adapter.tmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.MovieModel.Genre

class GenreAdapter(var genres: List<Genre>): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class GenreViewHolder(itemView: View, var listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        fun onBind(genre: Genre){
            itemView.findViewById<TextView>(R.id.genre_movie).text = genre.name
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genre_layout, parent, false), mListener
        )
    }

    override fun onBindViewHolder(holder: GenreAdapter.GenreViewHolder, position: Int) {
        holder.onBind(genres[position])
    }

    override fun getItemCount(): Int {
        return genres.size
    }
}