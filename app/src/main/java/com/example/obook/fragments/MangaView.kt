package com.example.obook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R
import com.example.obook.adapter.manga.MangaImageAdapter
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_manga_view.*

class MangaView : Fragment() {
    private lateinit var id: String
    private lateinit var mangaRecyclerView: RecyclerView
    private lateinit var mangaImage: ImageView
    private lateinit var next: Button
    private lateinit var prev: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manga_view, container, false)
        mangaImage = view.findViewById(R.id.manga_image_view)
        next = view.findViewById(R.id.next)
        prev = view.findViewById(R.id.prev)
//        mangaRecyclerView = view.findViewById<RecyclerView>(R.id.manga_recycler_view)
//        mangaRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        mangaRecyclerView.setHasFixedSize(true)
        id = arguments?.getString("id").toString()

        getImages(id)

        return view
    }

    private fun getImages(id: String){
        var index = 0
        MangaResponse.getMangaImages(id){
            val images = it.chapter.data.toMutableList()
            Log.d("images", images.toString())
            val hash = it.chapter.hash
            for(i in images.indices){
                images[i] = "https://uploads.mangadex.org/data/${hash}/${images[i]}"
                Log.d("images", images[i])
            }
            Picasso.get().load(images[0]).into(mangaImage)
            next.setOnClickListener {
                index++
                if(index < images.size-1){
                    Picasso.get().load(images[index]).into(mangaImage)
                    Log.d("images - next", images[index])
                }
            }
            prev.setOnClickListener {
                index--
                if(index >= 0){
                    Picasso.get().load(images[index]).into(mangaImage)
                }
            }



//            Log.d("images", hash.toString())
//            mangaRecyclerView.adapter = MangaImageAdapter(images, hash)
        }
    }

}