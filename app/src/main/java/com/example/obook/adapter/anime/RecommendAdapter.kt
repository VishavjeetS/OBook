package com.example.obook.adapter.anime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.model.animeModel.anime.recommendation.Data
import com.example.obook.R
import kotlinx.android.synthetic.main.movie_item.view.*

class RecommendAdapter(private val animeList: ArrayList<Data>): RecyclerView.Adapter<RecommendAdapter.AnimeViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    fun addData(newData: List<Data>) {
        animeList.addAll(newData)
        notifyDataSetChanged()
    }
    class AnimeViewHolder(itemView: View, private var listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        fun bindMovie(anime: Data){
            itemView.movie_title.text = anime.entry.title
            val image = anime.entry.images.jpg.imageUrl
            Glide.with(itemView.context).load(image).into(itemView.poster)
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