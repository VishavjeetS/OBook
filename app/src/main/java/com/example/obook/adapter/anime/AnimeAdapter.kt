package com.example.obook.adapter.anime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.model.animeModel.anime.topAnime.Data
//import com.example.obook.Model.AnimeModel.TopAnime.Images
import com.example.obook.R
import kotlinx.android.synthetic.main.movie_item.view.*

class AnimeAdapter(private val animeList: List<Data>): RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {
    lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }
    class AnimeViewHolder(itemView: View, private var listener: AnimeAdapter.OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        fun bindMovie(anime: Data){
            itemView.movie_title.text = anime.title
            val image = anime.images.webp.imageUrl
            if(image.isEmpty()){
                itemView.poster.setImageResource(R.drawable.ic_launcher_foreground)
            }
            else{
                Glide.with(itemView).load(anime.images.jpg.imageUrl).into(itemView.poster)
            }
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
        return animeList.size-1
    }
}