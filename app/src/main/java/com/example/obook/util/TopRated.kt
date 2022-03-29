package com.example.obook.util

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.MovieResponse
import com.example.obook.Model.Movies
import com.example.obook.R
import com.example.obook.detailActivity
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Comparator

class TopRated:Fragment() {
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.toprated, container, false)
        recyclerView = v.findViewById(R.id.movie_recycler_topRated)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getMovieData { movies: List<Movies> ->
            Collections.sort(movies,
                Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
            val adapter = MovieAdapter(movies)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireContext(), detailActivity::class.java)
                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                    Log.d("ImageUri", image)
                    intent.putExtra("image", image)
                    intent.putExtra("title", movies[position].title.toString())
                    intent.putExtra("overview", movies[position].overview.toString())
                    startActivity(intent)
                }
            })
        }
        return v
    }

    private fun getMovieData(callback: (List<Movies>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getTopRatedList().enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}