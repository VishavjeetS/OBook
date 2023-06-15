package com.example.obook.adapter.tmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.R
import kotlinx.android.synthetic.main.movie_item.view.*

class TvAdapter(private var tvList:MutableList<TV>): RecyclerView.Adapter<TvAdapter.TvViewHolder>(){
    lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int);
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun addData(newData: List<TV>) {
        tvList.addAll(newData)
        notifyDataSetChanged()
    }
    class TvViewHolder(itemView: View, private var listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        var title = ""
        fun bindMovie(movie: TV){
            itemView.movie_title.text = movie.title
            title = movie.title!!
            Glide.with(itemView).load(IMAGE_BASE + movie.poster_path).into(itemView.poster)
            itemView.alpha = 0F;
            itemView.animate().alpha(1F).start();
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        return TvViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false), mListener
        )
    }
    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bindMovie(tvList[position])
    }

    override fun getItemCount(): Int {
        return tvList.size
    }
}