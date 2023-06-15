package com.example.obook.adapter.anime

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.model.animeModel.anime.animeEpisodes.Data
import com.example.obook.R

class AnimeEpisodeAdapter(private var episodes: List<Data>): RecyclerView.Adapter<AnimeEpisodeAdapter.EpisodeViewHolder>() {

    class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(episode: Data){
            itemView.findViewById<TextView>(R.id.movie_title).text = episode.title.toString()
            Log.d("url", episode.url.toString())
//            Glide.with(itemView.context).load(episode.url).into(itemView.findViewById(R.id.image))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AnimeEpisodeAdapter.EpisodeViewHolder {
        return EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnimeEpisodeAdapter.EpisodeViewHolder, position: Int) {
        holder.onBind(episodes[position])
    }

    override fun getItemCount(): Int {
        return episodes.size
    }
}