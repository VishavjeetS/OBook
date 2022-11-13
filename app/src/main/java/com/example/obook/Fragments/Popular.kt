package com.example.obook.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ScrollView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.DetailActivity
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.R
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Popular:Fragment(), SearchView.OnQueryTextListener {
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MovieAdapter
    var page = 1
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val v = inflater.inflate(R.layout.popular, container, false)
        val nextBtn = v.findViewById<FloatingActionButton>(R.id.nextBtn)
        val backBtn = v.findViewById<FloatingActionButton>(R.id.backBtn)
        recyclerView = v.findViewById(R.id.movie_recycler)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.isLongClickable = true
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
        apiService.getMoviesList(page).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            }

        })
    }

    private fun searchMovie(callback: (List<Movies>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.searchMovie().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_items, menu)

        val searchView = menu.findItem(R.id.Search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        getMovieData {
            adapter = MovieAdapter(it as MutableList<Movies>)
            adapter.search(query)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }
}