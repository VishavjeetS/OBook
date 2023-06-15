package com.example.obook.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.adapter.manga.MangaChapterAdapter
import com.example.obook.adapter.manga.MangaVolumeAdapter
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse

class MangaDetail : Fragment() {
    private lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_manga_detail, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.volume_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.setHasFixedSize(true)

//        val bundle = arguments
//        val id = bundle?.getString("id")

        val id = arguments?.getString("id")
        val title = arguments?.getString("title")

        Log.d("id", id.toString())

        MangaResponse.getMangaChapters(id!!) { it ->
            val volumes = it.volumes.map{volume -> volume.value }.reversed().toList()
            val adapter = MangaVolumeAdapter(volumes)
            recyclerView.adapter = adapter
            adapter.setOnClickListener(object : MangaVolumeAdapter.OnClickListener{
                override fun onClick(position: Int, volume: String, recyclerView: RecyclerView) {
                    val chapters = volumes[position].chapters.map { chapter -> chapter.value }.reversed().toList()
                    val chapterAdapter = MangaChapterAdapter(chapters)
                    recyclerView.adapter = chapterAdapter
                    if(recyclerView.visibility == View.VISIBLE){
                        recyclerView.visibility = View.GONE
                    }else{
                        recyclerView.visibility = View.VISIBLE
                    }
                    chapterAdapter.setOnClickListener(object : MangaChapterAdapter.OnClickListener{
                        override fun onClick(position: Int, chapter: String) {
                            val intent = Intent(requireContext(), com.example.obook.MangaView::class.java)
                            intent.putExtra("id", chapter)
                            intent.putExtra("title", title)
                            intent.putExtra("chapter", chapters[position].chapter.toString())
                            startActivity(intent)
                            //makeScreen(MangaView(), chapter)
                            Log.d("chapter", chapters[position].chapter.toString())
                        }
                    })
                    Log.d("volumes", volume.toString())
                }
            })
        }
        return view
    }

    private fun makeScreen(fragment: Fragment, id: String){
        val bundle = Bundle()
        bundle.putString("id", id)
        requireFragmentManager().beginTransaction().apply {
            fragment.arguments = bundle
            replace(R.id.fragment_container_manga, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}