package com.example.obook.adapter.yts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.model.ytsModel.DownloadItem

class DownloadAdapter(var downloadItems: List<DownloadItem>): RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class DownloadViewHolder(itemView: View, var listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        fun onBind(downloadItem: DownloadItem) {
            itemView.findViewById<TextView>(R.id.magnet).text = downloadItem.option
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DownloadViewHolder {
        return DownloadViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.download_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.onBind(downloadItems[position])
    }

    override fun getItemCount(): Int {
        return downloadItems.size
    }
}