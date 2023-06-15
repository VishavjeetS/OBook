package com.example.obook.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.adapter.tmdb.TvAdapter
import com.example.obook.R
import com.example.obook.DetailActivity
import com.example.obook.adapter.anime.AnimeFullDetailAdapter
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.room.animeModel.AnimeDatabase
import com.example.obook.room.movieModel.MovieDatabase
import com.example.obook.room.tvModel.TvDatabase
import com.example.obook.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Favourite : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAvail: TextView
    private lateinit var mContext: Context
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
        moviesAvail = view.findViewById(R.id.movie_avail)
        recyclerView = view.findViewById(R.id.fav_rec)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        if(uInfo){
            tv.visibility = View.VISIBLE
        }

        when(Constant.getInstance().getScreen()){
            "TV" -> {
                val database = Room.databaseBuilder(requireContext(), TvDatabase::class.java, "tvDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                getFavTv(database)
                refresh.setOnClickListener {
                    getFavTv(database)
                }
                removeAll.setOnClickListener {
                    removeFromTvDB()
                }
            }
            "Movie" -> {
                val database = Room.databaseBuilder(requireContext(), MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                getFavMovies(database)
                refresh.setOnClickListener {
                    getFavMovies(database)
                }
                removeAll.setOnClickListener {
                    removeFromMovieDB()
                }
            }
            else -> {
                val database = Room.databaseBuilder(requireContext(), AnimeDatabase::class.java, "animeDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                getFavAnime(database)
                refresh.setOnClickListener {
                    getFavAnime(database)
                }
                removeAll.setOnClickListener {
                    removeFromAnimeDB()
                }
            }
        }
        return view
    }

    private fun getFavMovies(database: MovieDatabase) {
        database.movieDAO().getMovies().observe(requireActivity()) {
            if(it.isEmpty()){
                moviesAvail.visibility = View.VISIBLE
            }
            else{
                val adapter = MovieAdapter(it as MutableList<Movies>)
                recyclerView.adapter = adapter
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int, imageView: CardView) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra("id", it[position].id)
                        intent.putExtra("title", it[position].title.toString())
                        intent.putExtra("fav", true)
                        intent.putExtra("isMovie", true)
                        startActivity(intent)
                    }
                })
            }
        }
    }
    private fun getFavTv(database: TvDatabase){
        database.tvDAO().getTvShows().observe(requireActivity()) {
            if(it.isEmpty()){
                moviesAvail.visibility = View.VISIBLE
            }
            else{
                val adapter = TvAdapter(it as MutableList<TV>)
                recyclerView.adapter = adapter
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra("id", it[position].id)
                        intent.putExtra("title", it[position].title.toString())
                        intent.putExtra("isTv", true)
                        intent.putExtra("fav", true)
                        startActivity(intent)
                    }
                })
            }
        }
    }
    private fun getFavAnime(database: AnimeDatabase){
        database.animeDAO().getAnime().observe(requireActivity()) {
            if(it.isEmpty()){
                moviesAvail.visibility = View.VISIBLE
            }
            else{
                val adapter = AnimeFullDetailAdapter(it)
                recyclerView.adapter = adapter
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                adapter.setOnItemClickListener(object : AnimeFullDetailAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra("id", it[position].malId.toString())
                        intent.putExtra("title", it[position].title.toString())
                        intent.putExtra("anime", true)
                        intent.putExtra("fav", true)
                        startActivity(intent)
                    }
                })
            }
        }
    }
    private fun removeFromAnimeDB() {
        val database = Room.databaseBuilder(requireContext(), AnimeDatabase::class.java, "animeDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        database.animeDAO().nukeTable()
        getFavAnime(database)
    }
    private fun removeFromMovieDB() {
        val database = Room.databaseBuilder(requireContext(), MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        database.movieDAO().nukeTable()
        getFavMovies(database)
    }
    private fun removeFromTvDB(){
        val database = Room.databaseBuilder(requireContext(), TvDatabase::class.java, "tvDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
        database.tvDAO().nukeTable()
        getFavTv(database)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mContext = context
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnFragmentInteractionListener")
        }
    }
}
