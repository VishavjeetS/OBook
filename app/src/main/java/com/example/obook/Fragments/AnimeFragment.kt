package com.example.obook.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.R


class AnimeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.animeRecycle)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())


//        AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
//            .getRandomAnime().enqueue(object:Callback<Anime>{
//                override fun onResponse(
//                    call: Call<Anime>,
//                    response: Response<Anime>,
//                ) {
//                    Log.d("data", response.body().toString())
//                }
//
//                override fun onFailure(call: Call<Anime>, t: Throwable) {
//                    Log.d("error", t.message.toString())
//                }
//
//            })

        return view
    }
}