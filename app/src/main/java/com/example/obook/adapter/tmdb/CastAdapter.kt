package com.example.obook.adapter.tmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.model.CastModel.Cast
import com.example.obook.R
import kotlinx.android.synthetic.main.character_layout.view.*

open class CastAdapter(private var userList:MutableList<Cast>): RecyclerView.Adapter<CastAdapter.CastViewHolder>(){
    class CastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
        fun bindMovie(user: Cast){
//            itemView.user_name.text = user.name
//            itemView.character.text = "(${user.character})"
//            Glide.with(itemView).load(IMAGE_BASE + user.image).into(itemView.image)

            itemView.character_name.text = user.name
            itemView.character_character.text = "(${user.character})"
            itemView.character_card.setBackgroundResource(R.drawable.gradient_background)
            Glide.with(itemView).load(IMAGE_BASE + user.image).into(itemView.character_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.character_layout, parent, false)
        )
    }
    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bindMovie(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}