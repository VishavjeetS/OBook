package com.example.obook.adapter.anime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.model.animeModel.anime.animeCharacters.Data
import com.example.obook.R
import kotlinx.android.synthetic.main.character_layout.view.*
import kotlinx.android.synthetic.main.fragment_anime_detail.view.*

class CharacterAdapter(private var characters: List<Data>): RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        fun onBind(character: Data){
            val characterName = itemView.findViewById<TextView>(R.id.character_name)
            val characterImage = itemView.findViewById<ImageView>(R.id.character_image)
            itemView.character_character.visibility = View.GONE
            itemView.character_card.setBackgroundResource(R.color.dark_gray)
            var name = character.character.name
            characterName.text = name.replace(",", " ")
            Glide.with(itemView.context).load(character.character.images.jpg.imageUrl.toString()).into(characterImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CharacterViewHolder {
        return CharacterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.character_layout, parent, false) as ViewGroup)
    }

    override fun onBindViewHolder(holder: CharacterAdapter.CharacterViewHolder, position: Int) {
        holder.onBind(characters[position])
    }

    override fun getItemCount(): Int {
        return characters.size
    }
}