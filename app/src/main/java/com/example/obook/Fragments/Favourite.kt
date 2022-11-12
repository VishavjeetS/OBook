package com.example.obook.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.User
import com.example.obook.R
import com.example.obook.DetailActivity
import com.example.obook.Model.Movies
import com.example.obook.Room.MovieDatabase
import com.example.obook.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Favourite : Fragment() {
    lateinit var database: MovieDatabase
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        val obj = Constant.getInstance()
        val uInfo = obj.getInfo()
        val removeAll = view.findViewById<TextView>(R.id.removeAll)
        val refresh = view.findViewById<FloatingActionButton>(R.id.refresh)
        val tv = view.findViewById<TextView>(R.id.tv)
        val moviesAvail = view.findViewById<TextView>(R.id.movie_avail)
        println("User Info: " + obj.getInfo())
        println("User Info: " + uInfo)
        if(uInfo){
            tv.visibility = View.VISIBLE
        }
        database = Room.databaseBuilder(requireContext(), MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        database.movieDAO().getMovies().observe(requireActivity()){
            if(it.isEmpty()){
                moviesAvail.visibility = View.VISIBLE
            }
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.fav_rec)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        database.movieDAO().getMovies().observe(requireActivity()) {
            Log.d("Room", it.toString())
            val adapter = MovieAdapter(it as ArrayList<Movies>)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    val image = IMAGE_BASE + it[position].poster_path.toString()
                    Log.d("ImageUri", image)
                    intent.putExtra("image", image)
                    intent.putExtra("movieId", it[position].id)
                    intent.putExtra("title", it[position].title.toString())
                    intent.putExtra("overview", it[position].overview.toString())
                    intent.putExtra("date", it[position].release_date)
                    intent.putExtra("popularity", it[position].vote_count.toString())
                    intent.putExtra("fav", true)
                    startActivity(intent)
                }
            })
        }
        refresh.setOnClickListener {
            database.movieDAO().getMovies().observe(requireActivity()) {
                Log.d("Room", it.toString())
                val adapter = MovieAdapter(it as ArrayList<Movies>)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        val image = IMAGE_BASE + it[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("image", image)
                        intent.putExtra("movieId", it[position].id)
                        intent.putExtra("title", it[position].title.toString())
                        intent.putExtra("overview", it[position].overview.toString())
                        intent.putExtra("date", it[position].release_date)
                        intent.putExtra("popularity", it[position].vote_count.toString())
                        intent.putExtra("fav", true)
                        startActivity(intent)
                    }
                })
            }
        }
        removeAll.setOnClickListener {
            removeFromDB()
        }
        return view
    }

    private fun removeFromDB() {
        database.movieDAO().nukeTable()
    }
}
