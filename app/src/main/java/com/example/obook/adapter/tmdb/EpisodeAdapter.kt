package com.example.obook.adapter.tmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.TvModel.tvSeasonResponse.Episode
import com.squareup.picasso.Picasso

class EpisodeAdapter(var episodes: List<Episode>) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class EpisodeViewHolder(itemView: View, var listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        fun onBind(episode: Episode) {
//            itemView.findViewById<TextView>(R.id.title_episode).text = "${episode.episodeNumber}"
            itemView.findViewById<TextView>(R.id.episode_name).text = "${episode.episodeNumber}. ${episode.name}"
            itemView.findViewById<TextView>(R.id.episode_desc).text = episode.overview
            if(episode.stillPath == null || episode.stillPath.isEmpty()) Picasso.get().load(R.drawable.profile).into(itemView.findViewById<ImageView>(R.id.image_episode))
            else Picasso.get().load("https://image.tmdb.org/t/p/w500${episode.stillPath}").into(itemView.findViewById<ImageView>(R.id.image_episode))
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.episode_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.onBind(episodes[position])
    }

    override fun getItemCount(): Int {
        return episodes.size
    }
}