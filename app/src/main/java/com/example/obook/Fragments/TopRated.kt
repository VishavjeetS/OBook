package com.example.obook.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.R
import com.example.obook.DetailActivity
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Comparator

class TopRated:Fragment() {
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MovieAdapter
    @SuppressLint("NotifyDataSetChanged")
    var page = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.toprated, container, false)
        val nextBtn = v.findViewById<FloatingActionButton>(R.id.nextBtn)
        val backBtn = v.findViewById<FloatingActionButton>(R.id.backBtn)
        recyclerView = v.findViewById(R.id.movie_recycler_topRated)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        getMovieData { movies: List<Movies> ->
            Collections.sort(movies, Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
            adapter = MovieAdapter(movies as MutableList<Movies>)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                    Log.d("ImageUri", image)
                    intent.putExtra("image", image)
                    intent.putExtra("movieId", movies[position].id)
                    intent.putExtra("title", movies[position].title.toString())
                    intent.putExtra("overview", movies[position].overview.toString())
                    intent.putExtra("date", movies[position].release_date)
                    intent.putExtra("popularity", movies[position].vote_count.toString())
                    startActivity(intent)
                }
            })
        }
        nextBtn.setOnClickListener {
            page++
            getMovieData { movies: List<Movies> ->
                Collections.sort(movies,
                    Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                adapter = MovieAdapter(movies as MutableList<Movies>)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        val image = IMAGE_BASE + movies[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("movieId", movies[position].id)
                        intent.putExtra("image", image)
                        intent.putExtra("title", movies[position].title.toString())
                        intent.putExtra("overview", movies[position].overview.toString())
                        intent.putExtra("date", movies[position].release_date)
                        intent.putExtra("popularity", movies[position].vote_count.toString())
                        startActivity(intent)
                    }
                })
            }
        }
        backBtn.setOnClickListener {
            if(page-- > 1){
                getMovieData { movies: List<Movies> ->
                    Collections.sort(movies,
                        Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                    adapter = MovieAdapter(movies as MutableList<Movies>)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            val image = IMAGE_BASE + movies[position].poster_path.toString()
                            Log.d("ImageUri", image)
                            intent.putExtra("movieId", movies[position].id)
                            intent.putExtra("image", image)
                            intent.putExtra("title", movies[position].title.toString())
                            intent.putExtra("overview", movies[position].overview.toString())
                            intent.putExtra("date", movies[position].release_date)
                            intent.putExtra("popularity", movies[position].vote_count.toString())
                            startActivity(intent)
                        }
                    })
                }
            }
        }
        return v
    }

    private fun getMovieData(callback: (List<Movies>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getTopRatedList(page).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }

        })
    }
}