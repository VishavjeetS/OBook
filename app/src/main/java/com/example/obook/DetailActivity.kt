package com.example.obook

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.obook.Adapter.Anime.AnimeAdapter
import com.example.obook.Adapter.Anime.RecommendAdapter
import com.example.obook.Adapter.CastAdapter
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Adapter.TvAdapter
import com.example.obook.Model.AnimeModel.AnimeFullDetail.FullDetail
import com.example.obook.Model.AnimeModel.Recommendation.AnimeRecommendation
import com.example.obook.Model.CastModel.Cast
import com.example.obook.Model.CastModel.CastResponse
import com.example.obook.Model.KeywordModel.KeywordResponse
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.Model.TvModel.TV
import com.example.obook.Model.TvModel.TvResponse
import com.example.obook.Model.VideoModel.VideoResponse
import com.example.obook.Room.MovieModel.MovieDatabase
import com.example.obook.Room.TvModel.TvDatabase
import com.example.obook.services.Anime.AnimeApiInterface
import com.example.obook.services.Anime.AnimeApiService
import com.example.obook.services.Movie.MovieApiInterface
import com.example.obook.services.Movie.MovieApiService
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
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
    private var BTN_TEXT_ADD = "Add to favourite"
    private var BTN_TEXT_REMOVE = "Remove from favorites"
    private var id: String? = null
    private val APIKEY = "AIzaSyDtPM72fCs5vqVk77ukO2hcL-pBVLSMcHY" //Youtube
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    var Similar_page = 1
    private lateinit var Similar_list: MutableList<Movies>
    private lateinit var TV_Similar_list: MutableList<TV>
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
        val toolbar      = findViewById<Toolbar>(R.id.toolbar_detail)
        yT               = findViewById(R.id.yTFrag)
        val rating       = findViewById<TextView>(R.id.rating)
        var url          = Uri.parse(intent.getStringExtra("image"))
        var title        = intent.getStringExtra("title")
        id               = intent.getStringExtra("id")
        var overview     = intent.getStringExtra("overview")
        var date         = intent.getStringExtra("date")
        var voteCount    = intent.getStringExtra("popularity")

        //Movie
        val fav          = intent.getBooleanExtra("fav", false)
        val voteAvg      = intent.getStringExtra("voteAvg")

        //TV
        val TvFav        = intent.getBooleanExtra("TvFav", false)

        //Anime
        val airing = intent.getBooleanExtra("airing", false)
        val airFromDate = intent.getStringExtra("airFromDate")
        val duration = intent.getStringExtra("duration")
        val status = intent.getStringExtra("status")
        val episodes = intent.getStringExtra("episodes")
        val youtubeId = intent.getStringExtra("youtubeId")
        val isAnime = intent.getBooleanExtra("anime", false)
        var score = "${intent.getStringExtra(" score ")}/10"
        val obj          = Constant.getInstance()

        setSupportActionBar(toolbar)
        supportActionBar!!.title = title.toString()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back)?.apply {
            setTint(resources.getColor(R.color.white))
        }

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }

//        Log.d("id", id!!)
        recommendRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lifecycle.addObserver(yT)

        if(obj.getInfo()){
            fav_btn.text = "Please Sign in"
            fav_btn.isEnabled = false
        }

        Log.d("isTv", obj.getTv().toString())

        when(obj.getScreen()){
            "Movie" -> {
                val movie = Movies(id!!, title, voteCount, overview, url.toString(), date!!, voteAvg.toString())
                val database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
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
                                                intent.putExtra("id", data[position].id)
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
                                    intent.putExtra("id", data[position].id)
                                    intent.putExtra("title", data[position].title.toString())
                                    intent.putExtra("overview", data[position].overview.toString())
                                    intent.putExtra("date", data[position].release_date)
                                    intent.putExtra("popularity", data[position].vote_count.toString())
                                    startActivity(intent)
                                }
                            })
                        }

                        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                            Log.d("error - similarMovies", t.message.toString())
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
                                    intent.putExtra("id", data[position].id)
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
                Glide.with(this).load(url.toString()).into(detailImage)
                title1.text = title
                overview1.text = overview
                date1.text = date
                votes1.text = voteCount
                if(voteAvg.isNullOrBlank()){
                    rating.text = "?/10"
                }else{
                    rating.text = "$voteAvg/10"
                }
            }

            "TV" -> {
                val tv = TV(id!!, title, voteCount, overview, url.toString(), date!!, voteAvg.toString())
                val database = Room.databaseBuilder(this, TvDatabase::class.java, "tvDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                if(TvFav){
                    fav_btn.text = BTN_TEXT_REMOVE
                }
                database.tvDAO().getTvShows().observe(this as LifecycleOwner){
                    for(element in it){
                        if(element.id == tv.id){
                            fav_btn.text = BTN_TEXT_REMOVE
                        }
                    }
                }
                Log.d("Text", fav_btn.text.toString())

                if(fav_btn.text == BTN_TEXT_ADD){
                    fav_btn.setOnClickListener {
                        database.tvDAO().insertTvShow(tv)
                        Log.d("inserted - TV", tv.toString())
                        fav_btn.text = BTN_TEXT_REMOVE
                    }
                }
                else{
                    fav_btn.setOnClickListener {
                        database.tvDAO().deleteTvShow(tv)
                        fav_btn.text = BTN_TEXT_ADD
                    }
                }

                similarRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == TV_Similar_list.size - 1) {
                            //bottom of list!
                            Similar_page++
                            TvApiService.getInstance().create(TvApiInterface::class.java)
                                .getTvSimilar(id!!.toInt(), Similar_page).enqueue(object :Callback<TvResponse>{
                                    override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                                        TV_Similar_list = ArrayList()
                                        val data = response.body()!!.result
                                        TV_Similar_list = data as MutableList<TV>
                                        val adapter = TvAdapter(data as MutableList<TV>)
                                        similarRec.adapter = adapter
                                        adapter.notifyDataSetChanged()
                                        adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                val intent = Intent(applicationContext, DetailActivity::class.java)
                                                val image = IMAGE_BASE + data[position].poster_path.toString()
                                                Log.d("ImageUri", image)
                                                intent.putExtra("image", image)
                                                intent.putExtra("id", data[position].id)
                                                intent.putExtra("title", data[position].title.toString())
                                                intent.putExtra("overview", data[position].overview.toString())
                                                intent.putExtra("date", data[position].release_date)
                                                intent.putExtra("popularity", data[position].vote_count.toString())
                                                intent.putExtra("voteAvg", data[position].vote_average.toString())
                                                startActivity(intent)
                                            }
                                        })
                                    }

                                    override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                                        Log.d("error - similarMovies", t.message.toString())
                                    }

                                })
                        }
                    }
                })

                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvTrailer(id!!.toInt()).enqueue(object: Callback<VideoResponse> {
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

                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvCredits(id!!.toInt()).enqueue(object : Callback<CastResponse>{
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

                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvKeywords(id!!.toInt()).enqueue(object: Callback<KeywordResponse>{
                        override fun onResponse(
                            call: Call<KeywordResponse>,
                            response: Response<KeywordResponse>,
                        ) {
                            val data = response.body()!!.keywordsResult
                            var kw = ""
                            for(element in data){
                                kw += element.name + ", "
                            }
                            keyword.text = kw
                        }

                        override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                            Log.d("keyword error", t.message.toString())
                        }

                    })

                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvSimilar(id!!.toInt(), Similar_page).enqueue(object :Callback<TvResponse>{
                        override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                            TV_Similar_list = ArrayList()
                            val data = response.body()!!.result
                            TV_Similar_list = data as MutableList<TV>
                            Log.d("Movies Similar", data.toString())
                            val adapter = TvAdapter(data as MutableList<TV>)
                            similarRec.adapter = adapter
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(applicationContext, DetailActivity::class.java)
                                    val image = IMAGE_BASE + data[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("image", image)
                                    intent.putExtra("id", data[position].id)
                                    intent.putExtra("title", data[position].title.toString())
                                    intent.putExtra("overview", data[position].overview.toString())
                                    intent.putExtra("date", data[position].release_date)
                                    intent.putExtra("popularity", data[position].vote_count.toString())
                                    intent.putExtra("voteAvg", data[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }

                        override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                            Log.d("error - similarMovies", t.message.toString())
                        }

                    })

                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvRecommendation(id!!.toInt()).enqueue(object : Callback<TvResponse>{
                        override fun onResponse(
                            call: Call<TvResponse>,
                            response: Response<TvResponse>,
                        ) {
                            val data = response.body()!!.result
                            val adapter = TvAdapter(data as MutableList<TV>)
                            recommendRec.adapter = adapter
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(applicationContext, DetailActivity::class.java)
                                    val image = IMAGE_BASE + data[position].poster_path.toString()
                                    Log.d("ImageUri", image)
                                    intent.putExtra("image", image)
                                    intent.putExtra("id", data[position].id)
                                    intent.putExtra("title", data[position].title.toString())
                                    intent.putExtra("overview", data[position].overview.toString())
                                    intent.putExtra("date", data[position].release_date)
                                    intent.putExtra("popularity", data[position].vote_count.toString())
                                    intent.putExtra("voteAvg", data[position].vote_average.toString())
                                    startActivity(intent)
                                }
                            })
                        }

                        override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                Glide.with(this).load(url.toString()).into(detailImage)
                title1.text = title
                overview1.text = overview
                date1.text = date
                votes1.text = voteCount
                if(voteAvg.isNullOrBlank()){
                    rating.text = "?/10"
                }else{
                    rating.text = "$voteAvg/10"
                }
            }

            "Anime" -> {
                similarRec.visibility = View.GONE
                AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
                    .getAnimeRecommendations(id!!.toInt()).enqueue(object: Callback<AnimeRecommendation>{
                        override fun onResponse(call: Call<AnimeRecommendation>, response: Response<AnimeRecommendation>) {
                            val data = response.body()!!.data
                            val animeAdapter = RecommendAdapter(data)
                            recommendRec.adapter = animeAdapter
                            animeAdapter.notifyDataSetChanged()
                            animeAdapter.setOnItemClickListener(object : RecommendAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(applicationContext, DetailActivity::class.java)
                                    val image = data[position].entry.images.webp.imageUrl
                                    Log.d("ImageUri", image)
                                    intent.putExtra("id", data[position].entry.malId.toString())
                                    intent.putExtra("image", image)
                                    intent.putExtra("title", data[position].entry.title)
                                    intent.putExtra("anime", true)
                                    AnimeApiService.getInstance().create(AnimeApiInterface::class.java)
                                        .getFullDetail(data[position].entry.malId).enqueue(object: Callback<FullDetail>{
                                            override fun onResponse(
                                                call: Call<FullDetail>,
                                                response: Response<FullDetail>,
                                            ) {
                                                val data = response.body()!!.data
                                                id = data.malId.toString()
                                                url = Uri.parse(data.images.jpg.imageUrl)
                                                title = data.title
                                                overview = data.synopsis
                                                date = data.aired.from
                                                voteCount = data.popularity.toString()
                                                score= "${data.score}/10"

                                            }

                                            override fun onFailure(call: Call<FullDetail>, t: Throwable) {
                                                Log.d("error", t.message.toString())
                                            }

                                        })
                                    startActivity(intent)
                                }

                            })
                            yT.addYouTubePlayerListener(object :
                                AbstractYouTubePlayerListener() {
                                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                                    if(!youtubeId.isNullOrEmpty()){
                                        site.text = "Youtube"
                                        official.text = "Official"
                                        youTubePlayer.cueVideo(youtubeId, 0.00F)
                                    }
                                    else{
                                        site.text = "Video Not Available..."
                                        official.visibility = View.GONE
                                    }
                                }
                            })
                        }

                        override fun onFailure(call: Call<AnimeRecommendation>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })

                Glide.with(this).load(url.toString()).into(detailImage)
                title1.text = title
                overview1.text = overview
                date1.text = date
                votes1.text = voteCount
                if(score.isBlank()){
                    rating.text = "?/10"
                }else{
                    rating.text = score
                }
            }

            else -> {

            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        yT.release();
    }
}