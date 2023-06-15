package com.example.obook.adapter.anime

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.model.animeModel.anime.animeFullDetail.Genre
import com.example.obook.R

class AnimeGenreAdapter(private var genreList: List<Genre>) :
    RecyclerView.Adapter<AnimeGenreAdapter.GenreViewHolder>() {

    class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(genre: Genre){
            Log.d("Genre", genre.name)
            itemView.findViewById<TextView>(R.id.genre_movie).text = genre.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genre_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnimeGenreAdapter.GenreViewHolder, position: Int) {
        holder.onBind(genreList[position])
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
}