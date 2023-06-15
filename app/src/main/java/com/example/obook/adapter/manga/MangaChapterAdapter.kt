package com.example.obook.adapter.manga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.animeModel.Manga.mangadex.mangaChapters.Chapters
import kotlinx.android.synthetic.main.volumes_layout.view.*

class MangaChapterAdapter(private var chapterList: List<Chapters>): RecyclerView.Adapter<MangaChapterAdapter.MangaChapterViewHolder>() {

    private lateinit var mListener: OnClickListener

    interface OnClickListener{
        fun onClick(position: Int, chapter: String)
    }

    fun setOnClickListener(listener: OnClickListener){
        mListener = listener
    }

    class MangaChapterViewHolder(itemView: View, private var listener: OnClickListener) : RecyclerView.ViewHolder(itemView) {
        fun onBind(chapter: Chapters) {
            itemView.chapter_title.text = chapter.chapter
            itemView.setOnClickListener {
                itemView.setBackgroundColor(itemView.resources.getColor(R.color.dark_gray))
                listener.onClick(adapterPosition, chapter.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MangaChapterAdapter.MangaChapterViewHolder {
        return MangaChapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.volumes_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(
        holder: MangaChapterAdapter.MangaChapterViewHolder,
        position: Int,
    ) {
        holder.onBind(chapterList[position])
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }
}