package com.example.obook.adapter.yts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.ytsModel.YtsLinkModel

class TorrentLinkAdapter(private val torrentLinks: List<YtsLinkModel>): RecyclerView.Adapter<TorrentLinkAdapter.TorrentLinkViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class TorrentLinkViewHolder(itemView: View, val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        fun onBind(details: YtsLinkModel) {
            itemView.findViewById<TextView>(R.id.details).text = "${details.quality} - ${details.type}"
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentLinkViewHolder {
        return TorrentLinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.torrent_links_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: TorrentLinkViewHolder, position: Int) {
        holder.onBind(torrentLinks[position])
    }

    override fun getItemCount(): Int {
        return torrentLinks.size
    }
}