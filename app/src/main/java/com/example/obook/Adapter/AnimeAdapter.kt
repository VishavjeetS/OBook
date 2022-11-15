package com.example.obook.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.Model.Anime
//import com.example.obook.Model.Images
import com.example.obook.R
import kotlinx.android.synthetic.main.movie_item.view.*

class AnimeAdapter(private val animeList: List<Anime>): RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {
    lateinit var mListener: MovieAdapter.onItemClickListener
    interface onItemClickListener : MovieAdapter.onItemClickListener {
        override fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class AnimeViewHolder(itemView: View, private var listener: MovieAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView) {
        private val IMAGE_BASE = "https://api.jikan.moe/v4/"
        fun bindMovie(anime: Anime){
            itemView.movie_title.text = anime.title
//            Glide.with(itemView).load(IMAGE_BASE + Images().getJpg()).into(itemView.poster)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent,false)
        return AnimeViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bindMovie(animeList[position])
    }

    override fun getItemCount(): Int {
        return animeList.size
    }
}