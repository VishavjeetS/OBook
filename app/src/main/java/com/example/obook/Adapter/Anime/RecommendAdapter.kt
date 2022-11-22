package com.example.obook.Adapter.Anime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.AnimeModel.Recommendation.Data
import com.example.obook.R
import kotlinx.android.synthetic.main.movie_item.view.*

class RecommendAdapter(private val animeList: List<Data>): RecyclerView.Adapter<RecommendAdapter.AnimeViewHolder>() {
    lateinit var mListener: MovieAdapter.onItemClickListener
    interface onItemClickListener : MovieAdapter.onItemClickListener {
        override fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    class AnimeViewHolder(itemView: View, private var listener: MovieAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView) {
        fun bindMovie(anime: Data){
            itemView.movie_title.text = anime.entry.title
            val image = anime.entry.images.jpg.imageUrl
            Glide.with(itemView).load(image).into(itemView.poster)
//            Log.d("Image", anime.images.jpg.imageUrl)
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