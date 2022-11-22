package com.example.obook.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Adapter.Anime.AnimeAdapter
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Adapter.TvAdapter
import com.example.obook.DetailActivity
import com.example.obook.Model.AnimeModel.TopAnime.Anime
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.Model.TvModel.TV
import com.example.obook.Model.TvModel.TvResponse
import com.example.obook.R
import com.example.obook.services.Anime.AnimeApiInterface
import com.example.obook.services.Anime.AnimeApiService
import com.example.obook.services.Movie.MovieApiInterface
import com.example.obook.services.Movie.MovieApiService
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
import com.example.obook.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Popular:Fragment(){
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MovieAdapter
    lateinit var TvAdapter: TvAdapter
    lateinit var AnimeAdapter: AnimeAdapter
    var page = 1
    var tv_tp = 0
    var movie_tp = 0
    var isNextPage = false
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
        val constObj = Constant.getInstance()
        val isTv = constObj.getTv()
        Log.d("isTv", isTv.toString())
        when(constObj.getScreen()){
            "Movie" -> {
                getMovieData { movies: List<Movies> ->
                    Collections.sort(movies,
                        Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                    adapter = MovieAdapter(movies as MutableList<Movies>)
                    recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                    adapter.notifyDataSetChanged()
                    adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            val image = IMAGE_BASE + movies[position].poster_path.toString()
                            Log.d("ImageUri", image)
                            intent.putExtra("id", movies[position].id)
                            intent.putExtra("image", image)
                            intent.putExtra("title", movies[position].title.toString())
                            intent.putExtra("overview", movies[position].overview.toString())
                            intent.putExtra("date", movies[position].release_date)
                            intent.putExtra("popularity", movies[position].vote_count.toString())
                            intent.putExtra("voteAvg", movies[position].vote_average.toString())
                            startActivity(intent)
                        }
                    })
                }
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                        getMovieData { movies: List<Movies> ->
                            Collections.sort(movies,
                                Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                            adapter = MovieAdapter(movies as MutableList<Movies>)
                            recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(requireContext(), DetailActivity::class.java)
                                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("id", movies[position].id)
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", movies[position].title.toString())
                                    intent.putExtra("overview", movies[position].overview.toString())
                                    intent.putExtra("date", movies[position].release_date)
                                    intent.putExtra("popularity", movies[position].vote_count.toString())
                                    intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    if(page-- > 1){
                        Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                        getMovieData { movies: List<Movies> ->
                            Collections.sort(movies,
                                Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                            adapter = MovieAdapter(movies as MutableList<Movies>)
                            recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(requireContext(), DetailActivity::class.java)
                                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("id", movies[position].id)
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", movies[position].title.toString())
                                    intent.putExtra("overview", movies[position].overview.toString())
                                    intent.putExtra("date", movies[position].release_date)
                                    intent.putExtra("popularity", movies[position].vote_count.toString())
                                    intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }
                    }
                }
                nextBtn.setOnLongClickListener {
                    page = 500
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getMovieData { movies: List<Movies> ->
                        Collections.sort(movies,
                            Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                        adapter = MovieAdapter(movies as MutableList<Movies>)
                        recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                        adapter.notifyDataSetChanged()
                        adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(requireContext(), DetailActivity::class.java)
                                val image = IMAGE_BASE + movies[position].poster_path.toString()
                                Log.d("ImageUri", image)
                                intent.putExtra("id", movies[position].id)
                                intent.putExtra("image", image)
                                intent.putExtra("title", movies[position].title.toString())
                                intent.putExtra("overview", movies[position].overview.toString())
                                intent.putExtra("date", movies[position].release_date)
                                intent.putExtra("popularity", movies[position].vote_count.toString())
                                intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                startActivity(intent)
                            }
                        })
                    }
                    true
                }
                backBtn.setOnLongClickListener {
                    page = 1
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getMovieData { movies: List<Movies> ->
                        Collections.sort(movies,
                            Comparator<Movies> { lhs, rhs -> (rhs.vote_count!!.toInt()).compareTo(lhs.vote_count!!.toInt()) })
                        adapter = MovieAdapter(movies as MutableList<Movies>)
                        recyclerView.adapter = AlphaInAnimationAdapter(adapter)
                        adapter.notifyDataSetChanged()
                        adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(requireContext(), DetailActivity::class.java)
                                val image = IMAGE_BASE + movies[position].poster_path.toString()
                                Log.d("ImageUri", image)
                                intent.putExtra("id", movies[position].id)
                                intent.putExtra("image", image)
                                intent.putExtra("title", movies[position].title.toString())
                                intent.putExtra("overview", movies[position].overview.toString())
                                intent.putExtra("date", movies[position].release_date)
                                intent.putExtra("popularity", movies[position].vote_count.toString())
                                intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                startActivity(intent)
                            }
                        })
                    }
                    true
                }
            }
            "TV" -> {
                getTvPopularData { movies: List<TV> ->
                    Collections.sort(movies,
                        Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                    TvAdapter = TvAdapter(movies as MutableList<TV>)
                    recyclerView.adapter = AlphaInAnimationAdapter(TvAdapter)
                    TvAdapter.notifyDataSetChanged()
                    TvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            val image = IMAGE_BASE + movies[position].poster_path.toString()
                            Log.d("ImageUri", image)
                            intent.putExtra("id", movies[position].id)
                            intent.putExtra("image", image)
                            intent.putExtra("title", movies[position].title.toString())
                            intent.putExtra("overview", movies[position].overview.toString())
                            intent.putExtra("date", movies[position].release_date)
                            intent.putExtra("popularity", movies[position].vote_count.toString())
                            intent.putExtra("voteAvg", movies[position].vote_average.toString())
                            startActivity(intent)
                        }
                    })
                }
                nextBtn.setOnClickListener {
                    if(page<500){
                        page++
                        Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                        getTvPopularData { movies: List<TV> ->
                            Collections.sort(movies,
                                Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                            TvAdapter = TvAdapter(movies as MutableList<TV>)
                            recyclerView.adapter = AlphaInAnimationAdapter(TvAdapter)
                            TvAdapter.notifyDataSetChanged()
                            TvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(requireContext(), DetailActivity::class.java)
                                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("id", movies[position].id)
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", movies[position].title.toString())
                                    intent.putExtra("overview", movies[position].overview.toString())
                                    intent.putExtra("date", movies[position].release_date)
                                    intent.putExtra("popularity", movies[position].vote_count.toString())
                                    intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }
                    }else{
                        Toast.makeText(requireContext(), "Reached the end of page", Toast.LENGTH_SHORT).show()
                    }
                }
                backBtn.setOnClickListener {
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    if(page-- > 1){
                        getTvPopularData { movies: List<TV> ->
                            Collections.sort(movies,
                                Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                            TvAdapter = TvAdapter(movies as MutableList<TV>)
                            recyclerView.adapter = AlphaInAnimationAdapter(TvAdapter)
                            TvAdapter.notifyDataSetChanged()
                            TvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(requireContext(), DetailActivity::class.java)
                                    val image = IMAGE_BASE + movies[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("id", movies[position].id)
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", movies[position].title.toString())
                                    intent.putExtra("overview", movies[position].overview.toString())
                                    intent.putExtra("date", movies[position].release_date)
                                    intent.putExtra("popularity", movies[position].vote_count.toString())
                                    intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }

                    }
                }
                nextBtn.setOnLongClickListener {
                    page = 500
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getTvPopularData { movies: List<TV> ->
                        Collections.sort(movies,
                            Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                        TvAdapter = TvAdapter(movies as MutableList<TV>)
                        recyclerView.adapter = AlphaInAnimationAdapter(TvAdapter)
                        TvAdapter.notifyDataSetChanged()
                        TvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(requireContext(), DetailActivity::class.java)
                                val image = IMAGE_BASE + movies[position].poster_path.toString()
                                Log.d("ImageUri", image)
                                intent.putExtra("id", movies[position].id)
                                intent.putExtra("image", image)
                                intent.putExtra("title", movies[position].title.toString())
                                intent.putExtra("overview", movies[position].overview.toString())
                                intent.putExtra("date", movies[position].release_date)
                                intent.putExtra("popularity", movies[position].vote_count.toString())
                                intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                startActivity(intent)
                            }
                        })
                    }
                    true
                }
                backBtn.setOnLongClickListener {
                    page = 1
                    Toast.makeText(requireContext(), page.toString(), Toast.LENGTH_SHORT).show()
                    getTvPopularData { movies: List<TV> ->
                        Collections.sort(movies,
                            Comparator<TV> { lhs, rhs -> (rhs.vote_count!!.toDouble()).compareTo(lhs.vote_count!!.toDouble()) })
                        TvAdapter = TvAdapter(movies as MutableList<TV>)
                        recyclerView.adapter = AlphaInAnimationAdapter(TvAdapter)
                        TvAdapter.notifyDataSetChanged()
                        TvAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(requireContext(), DetailActivity::class.java)
                                val image = IMAGE_BASE + movies[position].poster_path.toString()
                                Log.d("ImageUri", image)
                                intent.putExtra("id", movies[position].id)
                                intent.putExtra("image", image)
                                intent.putExtra("title", movies[position].title.toString())
                                intent.putExtra("overview", movies[position].overview.toString())
                                intent.putExtra("date", movies[position].release_date)
                                intent.putExtra("popularity", movies[position].vote_count.toString())
                                intent.putExtra("voteAvg", movies[position].vote_average.toString())
                                startActivity(intent)
                            }
                        })
                    }
                    true
                }
            }
            "Anime" ->{
                AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
                    .getTopAnime(page).enqueue(object: Callback<Anime>{
                        override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                            val anime = response.body()!!.data
                            val pagination = response.body()!!.pagination
                            isNextPage = pagination.hasNextPage
                            AnimeAdapter = AnimeAdapter(anime)
                            recyclerView.adapter = AnimeAdapter
                            AnimeAdapter.notifyDataSetChanged()
                            AnimeAdapter.setOnItemClickListener(object : AnimeAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    Log.d("Id", anime[position].malId.toString())
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            val image = anime[position].images.jpg.imageUrl
                            Log.d("ImageUri", image)
                                    intent.putExtra("id", anime[position].malId.toString())
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", anime[position].title)
                                    intent.putExtra("overview", anime[position].synopsis)
                                    intent.putExtra("airing", anime[position].airing)
                                    intent.putExtra("airFromDate", anime[position].aired.from)
                                    intent.putExtra("popularity", anime[position].popularity.toString())
                                    intent.putExtra("duration", anime[position].duration)
                                    intent.putExtra("status", anime[position].status)
                                    intent.putExtra("episodes", anime[position].episodes.toString())
                                    intent.putExtra("score", anime[position].score.toString())
                                    intent.putExtra("youtubeId", anime[position].trailer.youtubeId)
                                    intent.putExtra("anime", true)
                            startActivity(intent)
                                }
                            })
                        }

                        override fun onFailure(call: Call<Anime>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                nextBtn.setOnClickListener {
                    if(isNextPage){
                        page++
                        AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
                            .getTopAnime(page).enqueue(object: Callback<Anime>{
                                override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                                    val anime = response.body()!!.data
                                    val pagination = response.body()!!.pagination
                                    AnimeAdapter = AnimeAdapter(anime)
                                    recyclerView.adapter = AnimeAdapter
                                    AnimeAdapter.notifyDataSetChanged()
                                    AnimeAdapter.setOnItemClickListener(object : AnimeAdapter.onItemClickListener {
                                        override fun onItemClick(position: Int) {
                                            Toast.makeText(requireContext(), anime[position].title, Toast.LENGTH_SHORT).show()
                                            val intent = Intent(requireContext(), DetailActivity::class.java)
                                            val image = anime[position].images.jpg.imageUrl.toString()
                                            Log.d("ImageUri", image)
                                            intent.putExtra("id", anime[position].malId.toString())
                                            intent.putExtra("image", image)
                                            intent.putExtra("title", anime[position].title)
                                            intent.putExtra("overview", anime[position].synopsis)
                                            intent.putExtra("airing", anime[position].airing)
                                            intent.putExtra("airFromDate", anime[position].aired.from)
                                            intent.putExtra("popularity", anime[position].popularity.toString())
                                            intent.putExtra("duration", anime[position].duration)
                                            intent.putExtra("status", anime[position].status)
                                            intent.putExtra("episodes", anime[position].episodes.toString())
                                            intent.putExtra("youtubeId", anime[position].trailer.youtubeId)
                                            intent.putExtra("anime", true)
                                            startActivity(intent)
                                        }
                                    })
                                }

                                override fun onFailure(call: Call<Anime>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                }
            }
        }
        return v
    }
    private fun getMovieData(callback: (List<Movies>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMoviesList(page).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                movie_tp = response.body()!!.tp.toInt()
                Log.d("totalMP", movie_tp.toString())
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            }

        })
    }

    private fun getTvPopularData(callback: (List<TV>) -> Unit){
        val apiService = TvApiService.getInstance().create(TvApiInterface::class.java)
        apiService.getTvList(page).enqueue(object: Callback<TvResponse> {
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                tv_tp = response.body()!!.tp.toInt()
                return callback(response.body()!!.result)
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}