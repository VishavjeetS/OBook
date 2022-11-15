package com.example.obook

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.obook.Adapter.CastAdapter
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.CastModel.Cast
import com.example.obook.Model.CastModel.CastResponse
import com.example.obook.Model.KeywordModel.KeywordResponse
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.Model.VideoModel.VideoResponse
import com.example.obook.Room.MovieDatabase
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import com.example.obook.util.Constant
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {
    lateinit var database: MovieDatabase
    private var BTN_TEXT_ADD = "Add to favourite"
    private var BTN_TEXT_REMOVE = "Remove from favorites"
    private var id: String? = null
    private val APIKEY = "AIzaSyDtPM72fCs5vqVk77ukO2hcL-pBVLSMcHY" //Youtube
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    var Similar_page = 1
    private lateinit var Similar_list: MutableList<Movies>
    private lateinit var yT: YouTubePlayerView
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val fav_btn      = findViewById<Button>(R.id.fav_btn)
        val recommendRec = findViewById<RecyclerView>(R.id.recommendRec)
        val similarRec   = findViewById<RecyclerView>(R.id.similar)
        val castRec      = findViewById<RecyclerView>(R.id.cast)
        val back         = findViewById<ImageView>(R.id.back)
        val toolbar      = findViewById<Toolbar>(R.id.toolbar_detail)
        yT               = findViewById(R.id.yTFrag)
        val rating       = findViewById<TextView>(R.id.rating)
        val url          = Uri.parse(intent.getStringExtra("image"))
        val title        = intent.getStringExtra("title")
        id               = intent.getStringExtra("movieId")
        val overview     = intent.getStringExtra("overview")
        val date         = intent.getStringExtra("date")
        val voteCount    = intent.getStringExtra("popularity")
        val fav          = intent.getBooleanExtra("fav", false)
        val voteAvg      = intent.getStringExtra("voteAvg")
        val movie        = Movies(id!!, title, voteCount, overview, url.toString(), date!!, voteAvg.toString())
        val obj          = Constant.getInstance()

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title.toString()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

        lifecycle.addObserver(yT)

        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        if(obj.getInfo()){
            fav_btn.text = "Please Sign in"
            fav_btn.isEnabled = false
        }

        Glide.with(this).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview
        date1.text = date
        votes1.text = voteCount
        if(voteAvg.isNullOrBlank()){
            rating.text = "?/10"
        }else{
            rating.text = "$voteAvg/10"
        }
        if(fav){
            fav_btn.text = BTN_TEXT_REMOVE
        }
        database.movieDAO().getMovies().observe(this as LifecycleOwner){
            for(element in it){
                if(element.id == movie.id){
                    fav_btn.text = BTN_TEXT_REMOVE
                }
            }
        }
        Log.d("Text", fav_btn.text.toString())

        if(fav_btn.text == BTN_TEXT_ADD){
            fav_btn.setOnClickListener {
                database.movieDAO().insertMovie(movie)
                Log.d("inserted", movie.toString())
                fav_btn.text = BTN_TEXT_REMOVE
            }
        }
        else{
            fav_btn.setOnClickListener {
                database.movieDAO().deleteMovie(movie)
                fav_btn.text = BTN_TEXT_ADD
            }
        }

//        back.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            this.finish()
//        }

        Log.d("id", id!!)
        recommendRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        similarRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == Similar_list.size - 1) {
                    //bottom of list!
                    Similar_page++
                    MovieApiService.getInstance().create(MovieApiInterface::class.java)
                    .getSimilar(id!!.toInt(), Similar_page).enqueue(object :Callback<MovieResponse>{
                        override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                            Similar_list = ArrayList()
                            val data = response.body()!!.movies
                            Similar_list = data as MutableList<Movies>
                            Log.d("Movies Similar", data.toString())
                            val adapter = MovieAdapter(data as MutableList<Movies>)
                            similarRec.adapter = adapter
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(applicationContext, DetailActivity::class.java)
                                    val image = IMAGE_BASE + data[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("image", image)
                                    intent.putExtra("movieId", data[position].id)
                                    intent.putExtra("title", data[position].title.toString())
                                    intent.putExtra("overview", data[position].overview.toString())
                                    intent.putExtra("date", data[position].release_date)
                                    intent.putExtra("popularity", data[position].vote_count.toString())
                                    startActivity(intent)
                                }
                            })
                        }

                        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
        })

        MovieApiService.getInstance().create(MovieApiInterface::class.java)
        .getSimilar(id!!.toInt(), Similar_page).enqueue(object :Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                Similar_list = ArrayList()
                val data = response.body()!!.movies
                Similar_list = data as MutableList<Movies>
                Log.d("Movies Similar", data.toString())
                val adapter = MovieAdapter(data as MutableList<Movies>)
                similarRec.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        val image = IMAGE_BASE + data[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("image", image)
                        intent.putExtra("movieId", data[position].id)
                        intent.putExtra("title", data[position].title.toString())
                        intent.putExtra("overview", data[position].overview.toString())
                        intent.putExtra("date", data[position].release_date)
                        intent.putExtra("popularity", data[position].vote_count.toString())
                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        MovieApiService.getInstance().create(MovieApiInterface::class.java)
        .getRecommendation(id!!.toInt()).enqueue(object : Callback<MovieResponse>{
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>,
            ) {
                val data = response.body()!!.movies
                val adapter = MovieAdapter(data as MutableList<Movies>)
                recommendRec.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(object : MovieAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        val image = IMAGE_BASE + data[position].poster_path.toString()
                        Log.d("ImageUri", image)
                        intent.putExtra("image", image)
                        intent.putExtra("movieId", data[position].id)
                        intent.putExtra("title", data[position].title.toString())
                        intent.putExtra("overview", data[position].overview.toString())
                        intent.putExtra("date", data[position].release_date)
                        intent.putExtra("popularity", data[position].vote_count.toString())
                        startActivity(intent)
                    }
                })
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        MovieApiService.getInstance().create(MovieApiInterface::class.java)
        .getMoviesTrailer(id!!.toInt()).enqueue(object: Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                val size = response.body()!!.videos.size-1
                for(i in 0..size){
                    val data = response.body()!!.videos[i]
                    if(data.official){
                        yT.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                                val videoId = data.key!!
                                site.text = data.site
                                official.text = "Official"
                                yT.isFullScreen()
                                yT.enterFullScreen();
                                yT.toggleFullScreen();
                                youTubePlayer.cueVideo(videoId, 0.00F)
                            }
                        })
                        yT.addFullScreenListener(object : YouTubePlayerFullScreenListener{
                            override fun onYouTubePlayerEnterFullScreen() {
                                yT.enterFullScreen();
                            }

                            override fun onYouTubePlayerExitFullScreen() {
                                yT.exitFullScreen();
                            }

                        });
                    }
                    else{
                        yT.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                                val videoId = data.key!!
                                site.text = data.site
                                official.text = "Not Official"
                                youTubePlayer.cueVideo(videoId, 0.00F)
                            }
                        })
                        yT.addFullScreenListener(object : YouTubePlayerFullScreenListener{
                            override fun onYouTubePlayerEnterFullScreen() {
                                yT.enterFullScreen();
                            }

                            override fun onYouTubePlayerExitFullScreen() {
                                yT.exitFullScreen();
                            }

                        });
                    }
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Log.d("site", t.message.toString())
            }

        })

        MovieApiService.getInstance().create(MovieApiInterface::class.java)
            .getCredits(id!!.toInt()).enqueue(object : Callback<CastResponse>{
                override fun onResponse(
                    call: Call<CastResponse>,
                    response: Response<CastResponse>,
                ) {
                    val data = response.body()!!.userList
                    val adapter = CastAdapter(data as MutableList<Cast>)
                    castRec.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }

            })

        MovieApiService.getInstance().create(MovieApiInterface::class.java)
            .getKeywords(id!!.toInt()).enqueue(object: Callback<KeywordResponse>{
                override fun onResponse(
                    call: Call<KeywordResponse>,
                    response: Response<KeywordResponse>,
                ) {
                    val data = response.body()!!.keywords
                    var kw = ""
                    for(i in 0..data.size-1){
                        kw += data[i].name + ", "
                    }
                    keyword.text = kw
                }

                override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
        yT.release();
    }
}