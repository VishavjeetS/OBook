package com.example.obook.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.DetailActivity
import com.example.obook.R
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.adapter.tmdb.TvAdapter
import com.example.obook.adapter.anime.AnimeAdapter
import com.example.obook.adapter.manga.MangaAdapter
import com.example.obook.adapter.tmdb.GenreAdapter
import com.example.obook.model.MovieModel.MovieResponse
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.model.TvModel.TvResponse
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
import com.example.obook.services.anime.AnimeApiInterface
import com.example.obook.services.anime.AnimeApiService
import com.example.obook.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.popular.*
import kotlinx.android.synthetic.main.popular.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Popular:Fragment(){
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    lateinit var movieAdapter: MovieAdapter
    private lateinit var popularGenre: RecyclerView
    lateinit var tvAdapter: TvAdapter
    lateinit var animeAdapter: AnimeAdapter
    private lateinit var mContext: Context
    var page = 1
    var tvTp = 0
    var movieTp = 0
    var isNextPage = false
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.popular, container, false)
        val nextBtn = view.findViewById<FloatingActionButton>(R.id.nextBtn)
        val backBtn = view.findViewById<FloatingActionButton>(R.id.backBtn)
        popularGenre = view.findViewById(R.id.popular_genre)
        recyclerView = view.findViewById(R.id.movie_recycler)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2).apply {
            isAutoMeasureEnabled = true
        }
        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.setHasFixedSize(true)
        recyclerView.isLongClickable = true
        popularGenre.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularGenre.setHasFixedSize(true)
        val constObj = Constant.getInstance()
        when(constObj.getScreen()){
            "Movie" -> {
                view.linear.visibility = View.VISIBLE
                getMovies()
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        getMovies()
                    }
                    else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- > 1){
                        getMovies()
                    }
                }
                nextBtn.setOnLongClickListener {
                    page = 500
                    getMovies()
                    true
                }
                backBtn.setOnLongClickListener {
                    page = 1
                    getMovies()
                    true
                }
            }
            "TV" -> {
                view.linear.visibility = View.VISIBLE
                getTv(1)
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        getTv(page)
                    }else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- >= 1){
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
            "Anime" ->{
                view.linear.visibility = View.VISIBLE
                getAnime(1)
                nextBtn.setOnClickListener {
                    if(isNextPage){
                        page++
                        getAnime(page)
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- >= 1) {
                        getAnime(page)
                    }
                }
            }
            "Manga" -> {
                getManga()
                nextBtn.visibility = View.GONE
                backBtn.visibility = View.GONE
//                view.linear.visibility = View.GONE
            }
        }
        return view
    }
    private fun getMovies(){
        MovieResponse.getGenre {
            val genre = it
            val genreAdapter = GenreAdapter(genre)
            popularGenre.adapter = genreAdapter
            genreAdapter.setOnItemClickListener(object : GenreAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.d("Genre", genre[position].name)
                }
            })
        }
        MovieResponse.getMoviePopular(page){
            movieTp = it.tp.toInt()
            val movies = it.movies
            Collections.sort(movies,
                Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
            movieAdapter = MovieAdapter(movies as MutableList<Movies>)
            recyclerView.adapter = AlphaInAnimationAdapter(movieAdapter)
            val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
            val controller = LayoutAnimationController(slideUpAnimation)
            recyclerView.layoutAnimation = controller
            recyclerView.setHasFixedSize(true)
            movieAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, imageView: CardView) {
                    val intent = Intent(mContext, DetailActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext as Activity, imageView, "image")
                    intent.putExtra("id", movies[position].id)
                    intent.putExtra("isMovie", true)
                    intent.putExtra("title", movies[position].title.toString())
                    mContext.startActivity(intent, options.toBundle())
                }
            })
        }
    }
    private fun getAnime(page: Int){
        AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
            .getTopAnime(page).enqueue(object: Callback<com.example.obook.model.animeModel.anime.topAnime.Anime>{
                override fun onResponse(call: Call<com.example.obook.model.animeModel.anime.topAnime.Anime>, response: Response<com.example.obook.model.animeModel.anime.topAnime.Anime>) {
                    val anime = response.body()!!.data
                    val pagination = response.body()!!.pagination
                    isNextPage = pagination.hasNextPage
                    animeAdapter = AnimeAdapter(anime)
                    recyclerView.adapter = AlphaInAnimationAdapter(animeAdapter)
                    val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                    val controller = LayoutAnimationController(slideUpAnimation)
                    recyclerView.layoutAnimation = controller
                    animeAdapter.setOnItemClickListener(object : AnimeAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            Log.d("Id", anime[position].malId.toString())
//                            makeCurrentScreen(AnimeDetail(), anime[position].malId)
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            val image = anime[position].images.jpg.imageUrl
//                            Log.d("ImageUri", image)
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
    private fun getTv(page: Int){
        val apiService = TvApiService.getInstance().create(TvApiInterface::class.java)
        apiService.getTvList(page).enqueue(object: Callback<TvResponse> {
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                tvTp = response.body()!!.tp.toInt()
                val tvShows = response.body()!!.result
                Collections.sort(tvShows,
                    Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                tvAdapter = TvAdapter(tvShows as MutableList<TV>)
                recyclerView.adapter = AlphaInAnimationAdapter(tvAdapter)
                val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
                val controller = LayoutAnimationController(slideUpAnimation)
                recyclerView.layoutAnimation = controller
                tvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        val image = IMAGE_BASE + tvShows[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("id", tvShows[position].id)
                        intent.putExtra("isTv", true)
                        intent.putExtra("title", tvShows[position].title.toString())
                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }
    private fun getManga(){
        MangaResponse.getMangaList(100){
            Log.d("Manga", it.size.toString())
            val mangaAdapter = MangaAdapter(it)
            recyclerView.layoutManager = GridLayoutManager(mContext, 3)
            recyclerView.adapter = mangaAdapter
//            val slideUpAnimation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
//            val controller = LayoutAnimationController(slideUpAnimation)
//            recyclerView.layoutAnimation = controller
            mangaAdapter.setOnClickListener(object : MangaAdapter.OnClickListener{
                override fun onClick(position: Int, id: String) {
                    Log.d("title", it[position].attributes.title.en!!)
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("manga", true)
                    intent.putExtra("title", it[position].attributes.title.en)
                    startActivity(intent)
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun makeCurrentScreen(fragment: Fragment, id: Int) = requireFragmentManager().beginTransaction().apply {
        val bundle = Bundle()
        bundle.putString("id", id.toString())
        fragment.arguments = bundle
        replace(R.id.wrapper_frame, fragment)
        commit()
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