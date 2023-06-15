package com.example.obook.adapter.manga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.animeModel.Manga.mangadex.mangaChapters.Volumes
import kotlinx.android.synthetic.main.volumes_layout.view.*

class MangaVolumeAdapter(private var volumes: List<Volumes>): RecyclerView.Adapter<MangaVolumeAdapter.MangaChapterViewHolder>() {

    private lateinit var mListener: OnClickListener

    interface OnClickListener{
        fun onClick(position: Int, volume: String, recyclerView: RecyclerView)
    }

    fun setOnClickListener(listener: OnClickListener){
        mListener = listener
    }

    class MangaChapterViewHolder(itemView: View, private var listener: MangaVolumeAdapter.OnClickListener) : RecyclerView.ViewHolder(itemView) {
        fun onBind(volume: Volumes) {
            itemView.chapter_title.text = volume.volume
            itemView.setOnClickListener {
                val chapterRecyclerView = itemView.findViewById<RecyclerView>(R.id.chapter_recycler_view)
                chapterRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                itemView.chapter_container.setBackgroundColor(itemView.resources.getColor(R.color.dark_gray))
                listener.onClick(adapterPosition, volume.volume, chapterRecyclerView)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MangaVolumeAdapter.MangaChapterViewHolder {
        return MangaChapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.volumes_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(
        holder: MangaVolumeAdapter.MangaChapterViewHolder,
        position: Int,
    ) {
        holder.onBind(volumes[position])
    }

    override fun getItemCount(): Int {
        return volumes.size
    }
}