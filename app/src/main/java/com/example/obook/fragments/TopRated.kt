package com.example.obook.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.adapter.anime.AnimeAdapter
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.adapter.tmdb.TvAdapter
import com.example.obook.model.MovieModel.MovieResponse
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.R
import com.example.obook.DetailActivity
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.model.TvModel.TvResponse
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.example.obook.services.Movie.MovieApiInterface
import com.example.obook.services.Movie.MovieApiService
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
import com.example.obook.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Comparator

class TopRated:Fragment() {
    private lateinit var mContext: Context
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    var page = 1
    var tvTp = 0
    var movieTp = 0
    var isNextPage = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.toprated, container, false)
        val nextBtn = view.findViewById<FloatingActionButton>(R.id.nextBtn)
        val backBtn = view.findViewById<FloatingActionButton>(R.id.backBtn)
        recyclerView = view.findViewById(R.id.movie_recycler_topRated)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val constObj = Constant.getInstance()
        val isTv = constObj.getTv()
        Log.d("isTv", isTv.toString())
        when(Constant.getInstance().getScreen()){
            "Movie" -> {
                getMovies(1)
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        getMovies(page)
                    }
                    else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- > 1){
                        getMovies(page)
                    }
                }
                nextBtn.setOnLongClickListener {
                    page = 500
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getMovies(page)
                    true
                }
                backBtn.setOnLongClickListener {
                    page = 1
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getMovies(page)
                    true
                }
            }
            "TV" -> {
                getTv(1)
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        getTv(page)
                    }
                    else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- > 1){
                        getTv(page)
                    }
                }
                nextBtn.setOnLongClickListener {
                    page = 500
                    getTv(page)
                    true
                }
                backBtn.setOnLongClickListener {
                    page = 1
                    getTv(page)
                    true
                }
            }
            else -> {
                getAnime(5)
                nextBtn.setOnClickListener {
                    if(isNextPage){
                        page++
                        getAnime(page)
                    }
                    else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- > 5){
                        getAnime(page)
                    }
                }
            }
        }
        return view
    }

    private fun getMovies(page: Int){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getTopRatedList(page).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                movieTp = response.body()!!.tp.toInt()
                val movies = response.body()!!.movies
                Collections.sort(movies, Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                val adapter = MovieAdapter(movies as MutableList<Movies>)
                recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int, imageView: CardView) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        val image = IMAGE_BASE + movies[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("image", image)
                        intent.putExtra("id", movies[position].id)
                        intent.putExtra("isMovie", true)
                        intent.putExtra("title", movies[position].title.toString())
                        intent.putExtra("overview", movies[position].overview.toString())
                        intent.putExtra("date", movies[position].release_date)
                        intent.putExtra("popularity", movies[position].vote_count.toString())
                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }

        })
    }
    private fun getTv(page: Int){
        val apiService = TvApiService.getInstance().create(TvApiInterface::class.java)
        apiService.getTvTrendingList(page).enqueue(object: Callback<TvResponse> {
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                tvTp = response.body()!!.tp.toInt()
                val tvShows = response.body()!!.result
                Collections.sort(tvShows, Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                val adapter = TvAdapter(tvShows as MutableList<TV>)
                recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        val image = IMAGE_BASE + tvShows[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("image", image)
                        intent.putExtra("id", tvShows[position].id)
                        intent.putExtra("isTv", true)
                        intent.putExtra("title", tvShows[position].title.toString())
                        intent.putExtra("overview", tvShows[position].overview.toString())
                        intent.putExtra("date", tvShows[position].release_date)
                        intent.putExtra("popularity", tvShows[position].vote_count.toString())
                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }

    private fun getAnime(page: Int){
        AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
            .getTopAnime(page).enqueue(object: Callback<com.example.obook.model.animeModel.anime.topAnime.Anime>{
                override fun onResponse(call: Call<com.example.obook.model.animeModel.anime.topAnime.Anime>, response: Response<com.example.obook.model.animeModel.anime.topAnime.Anime>) {
                    val anime = response.body()!!.data
                    val pagination = response.body()!!.pagination
                    isNextPage = pagination.hasNextPage
                    val animeAdapter = AnimeAdapter(anime)
                    recyclerView.adapter = animeAdapter
                    val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                    val controller = LayoutAnimationController(slideUpAnimation)
                    recyclerView.layoutAnimation = controller
                    animeAdapter.setOnItemClickListener(object : AnimeAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            intent.putExtra("id", anime[position].malId.toString())
                            intent.putExtra("title", anime[position].title)
                            intent.putExtra("anime", true)
                            startActivity(intent)
                        }
                    })
                }

                override fun onFailure(call: Call<com.example.obook.model.animeModel.anime.topAnime.Anime>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mContext = context
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FragmentListener")
        }
    }
}