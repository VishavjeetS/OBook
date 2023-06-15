package com.example.obook.adapter.manga

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.R
import com.squareup.picasso.Picasso

class MangaImageAdapter(private var mangaImageList: List<String>, hash: String) :
    RecyclerView.Adapter<MangaImageAdapter.MangaImageClassViewHolder>() {

    private val baseHash = hash

    class MangaImageClassViewHolder(itemView: View, private val hash: String) : RecyclerView.ViewHolder(itemView) {
        private val baseUrl = "https://uploads.mangadex.org/data/"
        val nextButton: Button = itemView.findViewById(R.id.next_button)
        val previousButton: Button = itemView.findViewById(R.id.previous_button)
        fun onBind(mangaImage: String) {
            val mangaImageLink = "$baseUrl$hash/$mangaImage"
            Log.d("mangaImageLink", mangaImageLink)
            Picasso.get().load(mangaImageLink).into(itemView.findViewById<ImageView>(R.id.manga_image))
//            Glide.with(itemView.context).load(mangaImageLink)
//                .into(itemView.findViewById(R.id.manga_image))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MangaImageAdapter.MangaImageClassViewHolder {
        return MangaImageClassViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.manga_image_viewer, parent, false), baseHash
        )
    }

    override fun onBindViewHolder(
        holder: MangaImageAdapter.MangaImageClassViewHolder,
        position: Int,
    ) {
        holder.onBind(mangaImageList[position])
        holder.nextButton.setOnClickListener {
            if (position < mangaImageList.size - 1) {
                holder.onBind(mangaImageList[position+1])
            }
        }
        holder.previousButton.setOnClickListener {
            if(position==0){
                holder.onBind(mangaImageList[position])
            }
            else{
                holder.onBind(mangaImageList[position-1])
            }
        }
    }

    override fun getItemCount(): Int {
        return mangaImageList.size
    }

}