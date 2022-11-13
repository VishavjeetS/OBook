package com.example.obook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.obook.Model.Movies
import com.example.obook.Model.VideoResponse
import com.example.obook.Room.MovieDatabase
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import com.example.obook.util.Constant
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : YouTubeBaseActivity() {
    lateinit var database: MovieDatabase
    private var BTN_TEXT_ADD = "Add to favourite"
    private var BTN_TEXT_REMOVE = "Remove from favorites"
    private var id: String? = null
    private val APIKEY = "AIzaSyDtPM72fCs5vqVk77ukO2hcL-pBVLSMcHY" //Youtube
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

        val YT = findViewById<YouTubePlayerView>(R.id.yTFrag)

        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        Glide.with(this).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview
        date1.text = date
        votes1.text = voteCount

        Log.d("True Value ", obj.getInfo().toString())
        if(obj.getInfo()){
            fav_btn.text = "Please Sign in"
            fav_btn.isClickable = false
        }

        if (fav){
            fav_btn.text = BTN_TEXT_REMOVE
        }

//        database.movieDAO().getMovies().observe(this as LifecycleOwner){
//            if(it.contains(movie)){
//                fav_btn.text = BTN_TEXT_REMOVE
//            }
//        }

        Log.d("btnText", fav_btn.text.toString())

        if(fav_btn.text == BTN_TEXT_ADD){
            fav_btn.setOnClickListener {
                database.movieDAO().insertMovie(movie)
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

        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMoviesTrailer(id!!.toInt()).enqueue(object: Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                val size = response.body()!!.videos.size-1
                for(i in 0..size){
                    val data = response.body()!!.videos[i]
                    if(data.official && data.site == "YouTube"){
                        YT.initialize(APIKEY, object : YouTubePlayer.OnInitializedListener{
                            override fun onInitializationSuccess(
                                provider: YouTubePlayer.Provider?,
                                player: YouTubePlayer?,
                                wasRestored: Boolean,
                            ) {
                                if(player==null) return
                                if(wasRestored){
                                    player.play()
                                }
                                else{
                                    player.cueVideo(data.key)
                                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                                }
                            }

                            override fun onInitializationFailure(
                                p0: YouTubePlayer.Provider?,
                                p1: YouTubeInitializationResult?,
                            ) {
                                Log.d("Error", p1!!.name)
                            }

                        })
                    }
                    else{
                        YT.visibility = View.GONE
                        site.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Log.d("site", t.message.toString())
            }

        })
    }
}