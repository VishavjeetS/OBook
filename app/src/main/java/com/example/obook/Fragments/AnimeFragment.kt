package com.example.obook.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Model.AnimeResponse
import com.example.obook.R
import com.example.obook.services.AnimeApiInterface
import com.example.obook.services.AnimeApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnimeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_anime, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.animeRecycle)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
            .getRandomAnime().enqueue(object:Callback<AnimeResponse>{
                override fun onResponse(
                    call: Call<AnimeResponse>,
                    response: Response<AnimeResponse>,
                ) {
                    Log.d("data", response.body().toString())
                }

                override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })

        return view
    }
}