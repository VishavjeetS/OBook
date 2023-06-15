package com.example.obook.adapter.manga

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.animeModel.Manga.mangadex.mangaList.Data
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.manga_layout.view.*

class MangaAdapter(private var mangaList: List<Data>): RecyclerView.Adapter<MangaAdapter.MangaViewHolder>() {

    private lateinit var mListener: MangaAdapter.OnClickListener

    interface OnClickListener{
        fun onClick(position: Int, id: String)
    }

    fun setOnClickListener(listener: MangaAdapter.OnClickListener){
        mListener = listener
    }

    class MangaViewHolder(itemView: View, private val listener: OnClickListener): RecyclerView.ViewHolder(itemView) {
        fun onBind(manga: Data){
            Log.d("manga", manga.attributes.title.toString())
            itemView.progressBar.visibility = View.VISIBLE
            if(manga.attributes.availableTranslatedLanguages.contains("en")){
                if(manga.attributes.title.en != null){
                    MangaResponse.getMangaImage(manga.attributes.title.en){
                        if(it.isNotEmpty()){
                            Picasso.get().load(it[0].images.jpg.imageUrl).into(itemView.manga_image)
                            itemView.progressBar.visibility = View.GONE
                        }
                        else{
                            itemView.manga_image.setImageResource(R.drawable.profile)
                            itemView.progressBar.visibility = View.GONE
                        }
                    }
                    if( manga.attributes.title.en.toString().length > 20) {
                        val title = manga.attributes.title.en.substring(0, 20) + "..."
                        itemView.manga_name.text = title
                    }
                    else{
                        itemView.manga_name.text = manga.attributes.title.en
                    }
                }
                MangaResponse.getMangaCover(manga.id){
                    Log.d("manga", it.attributes.fileName?:"")
                }

            }
//            else{
//                itemView.manga_name.text = manga.attributes.title.en_jp
//                MangaResponse.getMangaImage(manga.attributes.title.en_jp?:""){
//                    if (it.isNotEmpty()) {
//                        Picasso.get().load(it[0].images.jpg.imageUrl).into(itemView.manga_image)
//                        itemView.progressBar.visibility = View.GONE
//                    }
//                    else {
//                        itemView.manga_image.setImageResource(R.drawable.profile)
//                        itemView.progressBar.visibility = View.GONE
//                    }
//
//                }
//            }
            itemView.setOnClickListener {
                listener.onClick(adapterPosition, manga.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MangaAdapter.MangaViewHolder {
        return MangaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.manga_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MangaAdapter.MangaViewHolder, position: Int) {
        holder.onBind(mangaList[position])
    }

    override fun getItemCount(): Int {
        return mangaList.size-1
    }
}