package com.example.obook

//import com.google.android.youtube.player.YouTubePlayer
//import com.google.android.youtube.player.YouTubePlayerView
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.obook.Adapter.MovieAdapter
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
    private lateinit var yT: YouTubePlayerView
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val url = Uri.parse(intent.getStringExtra("image"))
        val title = intent.getStringExtra("title")
        id = intent.getStringExtra("movieId")
        val overview = intent.getStringExtra("overview")
        val date = intent.getStringExtra("date")
        val voteCount = intent.getStringExtra("popularity")
        val fav = intent.getBooleanExtra("fav", false)
        val movie = Movies(id!!, title, voteCount, overview, url.toString(), date!!)
        val obj = Constant.getInstance()
        val fav_btn = findViewById<Button>(R.id.fav_btn)

        yT = findViewById(R.id.yTFrag)
        lifecycle.addObserver(yT)

        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        Glide.with(this).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview
        date1.text = date
        votes1.text = voteCount

        if(obj.getInfo()){
            fav_btn.text = "Please Sign in"
            fav_btn.isClickable = false
        }


        database.movieDAO().getMovies().observe(this as LifecycleOwner){
            if(it.contains(movie)){
                fav_btn.text = BTN_TEXT_REMOVE
            }
        }

        if(fav){
            fav_btn.text = BTN_TEXT_REMOVE
        }

        Log.d("Fav Movie", fav.toString())
        Log.d("btnText", fav_btn.text.toString())

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

        Log.d("id", id!!)
        val recommendRec = findViewById<RecyclerView>(R.id.recommendRec)
        recommendRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val apiServiceRecommend = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiServiceRecommend.getRecommendation(id!!.toInt()).enqueue(object : Callback<MovieResponse>{
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


        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMoviesTrailer(id!!.toInt()).enqueue(object: Callback<VideoResponse> {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        yT.release();
    }
}