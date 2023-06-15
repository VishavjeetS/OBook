package com.example.obook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.example.obook.adapter.tmdb.CastAdapter
import com.example.obook.adapter.tmdb.EpisodeAdapter
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.adapter.tmdb.TvAdapter
import com.example.obook.adapter.yts.DownloadAdapter
import com.example.obook.adapter.yts.TorrentLinkAdapter
import com.example.obook.fragments.AnimeDetail
import com.example.obook.fragments.MangaDetail
import com.example.obook.model.CastModel.Cast
import com.example.obook.model.KeywordModel.Keywords
import com.example.obook.model.MovieModel.MovieResponse
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.model.TvModel.TvResponse
import com.example.obook.model.VideoModel.Videos
import com.example.obook.model.ytsModel.DownloadItem
import com.example.obook.model.ytsModel.YtsLinkModel
import com.example.obook.room.movieModel.MovieDatabase
import com.example.obook.room.tvModel.TvDatabase
import com.example.obook.services.yts.YtsApiService
import com.example.obook.services.yts.YtsResponse
import com.example.obook.util.Constant
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_anime.*
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.popular.view.*
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {
    private var BTN_TEXT_ADD = "Add to favourite"
    private var BTN_TEXT_REMOVE = "Remove from favorites"
    private var id: String? = null
    private var fav: Boolean = false
    private val APIKEY = "AIzaSyDtPM72fCs5vqVk77ukO2hcL-pBVLSMcHY" //Youtube
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    private lateinit var Similar_list: MutableList<Movies>
    private lateinit var yT: YouTubePlayerView
    private lateinit var detailLayout: LinearLayout
    private lateinit var playerEventListener: Player.Listener
    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var exoPlayerView: com.google.android.exoplayer2.ui.PlayerView

    private lateinit var recommendRec: RecyclerView
    private lateinit var similarRec: RecyclerView
    private lateinit var castRec: RecyclerView
    private lateinit var discoverRec: RecyclerView
    private lateinit var favBtn: Button
    private lateinit var playBtn: ImageView
    private lateinit var downloadBtn: ImageView
    private lateinit var spinner: Spinner
    private lateinit var seasonRecyclerView: RecyclerView

    var isStream = false

    var page = 1

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(YtsApiService(), filter)

//        window.enterTransition = TransitionInflater.from(this)
//            .inflateTransition(R.transition.image_transition)
//        window.exitTransition = TransitionInflater.from(this)
//            .inflateTransition(R.transition.image_transition)

        supportPostponeEnterTransition();

//        val transition = window.sharedElementEnterTransition
//        transition.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        recommendRec = findViewById(R.id.recommendRec)
        similarRec = findViewById(R.id.similar)
        castRec = findViewById(R.id.cast)
        discoverRec = findViewById(R.id.discoverMoviesRV)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
        favBtn = findViewById(R.id.fav_btn)
        detailLayout = findViewById(R.id.detailLayout)
        yT = findViewById(R.id.yTFrag)
        playBtn = findViewById(R.id.play_button)
        downloadBtn = findViewById(R.id.download_button)
        val view = findViewById<View>(R.id.layout)
        spinner = view.findViewById(R.id.spinner)
        seasonRecyclerView = view.findViewById(R.id.season_recycler_view)

        // Get data from intent
        id = intent.getStringExtra("id")
        fav = intent.getBooleanExtra("fav", false)
        val isMovie = intent.getBooleanExtra("isMovie", false)
        val isTv = intent.getBooleanExtra("isTv", false)
        val isAnime = intent.getBooleanExtra("anime", false)
        val isManga = intent.getBooleanExtra("manga", false)
        val mAuth = FirebaseAuth.getInstance()
        val obj = Constant.getInstance()

        setSupportActionBar(toolbar)
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

        playBtn.setOnClickListener {
            isStream = true
            getTorrentLinks()
        }

        downloadBtn.setOnClickListener {
            isStream = false
            getTorrentLinks()
        }


        recommendRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendRec.setHasFixedSize(true)
        similarRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarRec.setHasFixedSize(true)
        castRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRec.setHasFixedSize(true)
        discoverRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        discoverRec.setHasFixedSize(true)

        lifecycle.addObserver(yT)


        if (mAuth.currentUser == null) {
            favBtn.text = "Please Sign in"
            favBtn.isEnabled = false
        }

        if (isTv) {
            getTvDetails(id!!.toInt())
        } else if (isMovie) {
            getMovieDetails(id!!.toInt())
        } else if (isAnime) {
            getAnimeDetails(id!!.toInt())
        } else if (isManga) {
            getMangaDetails(id)
        }


        when (obj.getScreen()) {
            "Movie" -> {
                playBtn.visibility = View.VISIBLE
                getMovies()
            }

            "TV" -> {
                getTv()
            }
        }
    }

    private fun getMangaDetails(id: String?) {
        relativeLayout.visibility = View.GONE
        detailLayout.visibility = View.GONE
        supportActionBar!!.title = intent.getStringExtra("title")
        supportFragmentManager.beginTransaction().apply {
            val fragment = MangaDetail()
            val bundle = Bundle()
            bundle.putString("id", id.toString())
            bundle.putString("title", intent.getStringExtra("title"))
            fragment.arguments = bundle
            replace(R.id.detail_frame, fragment)
            commit()
        }
    }


    private fun getTv() {
        layout.visibility = View.VISIBLE;
        val database = Room.databaseBuilder(this, TvDatabase::class.java, "tvDB")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()

        doDatabaseStuff(database, favBtn, fav)

        TvResponse.getTvDetails(id!!.toInt()){
            val tvId = id!!.toInt()
            run{
                val tvDetail = it
                val seasonList = mutableListOf<String>()
                for (season in tvDetail.seasons){
                    seasonList.add(season.name)
                }
                spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, seasonList)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        Log.d("TAG", "onItemSelected: ${tvDetail.seasons[position].name}")
                        val seasonNum = tvDetail.seasons[position].seasonNumber
                        TvResponse.getEpisodes(tvId, seasonNum){
                            run{
                                val adapter = EpisodeAdapter(it)
                                seasonRecyclerView.layoutManager = GridLayoutManager(this@DetailActivity, 2, GridLayoutManager.HORIZONTAL, false)
//                                seasonRecyclerView.layoutManager = LinearLayoutManager(this@DetailActivity).apply {
//                                    orientation = LinearLayoutManager.HORIZONTAL
//                                }
                                seasonRecyclerView.adapter = adapter
                                adapter.setOnItemClickListener(object : EpisodeAdapter.OnItemClickListener{
                                    override fun onItemClick(position: Int) {
                                        Log.d("TAG", "onItemClick: ${it[position].name}")
//                                        val episode = it[position]
//                                        val intent = Intent(this@DetailActivity, PlayerActivity::class.java)
//                                        intent.putExtra("id", episode.id)
//                                        intent.putExtra("title", episode.name)
//                                        intent.putExtra("season", seasonNum)
//                                        intent.putExtra("episode", episode.episodeNumber)
//                                        intent.putExtra("isTv", true)
//                                        startActivity(intent)
                                    }
                                })
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO()
                    }
                }
            }
        }

        TvResponse.getTvTrailers(id!!.toInt()) { videos: List<Videos> ->
            run {
                getTrailers(videos)
            }
        }

        TvResponse.getTvCredits(id!!.toInt()) { cast: List<Cast> ->
            run {
                val adapter = CastAdapter(cast as MutableList<Cast>)
                castRec.adapter = adapter
            }
        }

        TvResponse.getTvKeywords(id!!.toInt()) { keywords: List<Keywords> ->
            run {
                var kw = ""
                for (element in keywords) {
                    kw += element.name + ", "
                }
                if(kw.isNotEmpty()) keyword.text = kw
                else keyword.text = "No Keywords"
            }
        }

        TvResponse.getTvSimilar(id!!.toInt(), page) { similar: List<TV> ->
            run {
                val adapter = TvAdapter(similar as MutableList<TV>)
                similarRec.adapter = adapter
                similarRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollHorizontally(1)) {
                            TvResponse.getTvSimilar(id!!.toInt(), page + 1) {
                                val similarList = it
                                adapter.addData(similarList)
                            }
                        }
                    }
                })
                adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra("id", similar[position].id)
                        intent.putExtra("title", similar[position].title.toString())
                        intent.putExtra("isTv", true)
                        startActivity(intent)
                    }
                })
            }
        }

        TvResponse.getTvRecommendation(id!!.toInt(), page) { recommendation: List<TV> ->
            run {
                val adapter = TvAdapter(recommendation as MutableList<TV>)
                recommendRec.adapter = adapter
                recommendRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollHorizontally(1)) {
                            TvResponse.getTvRecommendation(id!!.toInt(), page + 1) {
                                val tvList = it
                                adapter.addData(tvList)
                            }
                        }
                    }
                })
                adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra("id", recommendation[position].id)
                        intent.putExtra("isTv", true)
                        intent.putExtra("title", recommendation[position].title.toString())
                        startActivity(intent)
                    }
                })
            }
        }

        TvResponse.getDiscoverTv(page) { discover: List<TV> ->
            run {
                val adapter = TvAdapter(discover as MutableList<TV>)
                discoverRec.adapter = adapter
                discoverRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollHorizontally(1)) {
                            TvResponse.getDiscoverTv(page + 1) {
                                adapter.addData(it)
                            }
                        }
                    }
                })
                adapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra("id", discover[position].id)
                        intent.putExtra("title", discover[position].title.toString())
                        intent.putExtra("isTv", true)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun getMovies() {
        layout.visibility = View.GONE;
        val database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()

        doDatabaseStuff(database, favBtn, fav)



        MovieResponse.getMovieTrailers(id!!.toInt()) { videos: List<Videos> ->
            run {
                getTrailers(videos)
            }

        }

        MovieResponse.getMovieCredits(id!!.toInt()) { credits: List<Cast> ->
            run {
                val adapter = CastAdapter(credits as MutableList<Cast>)
                castRec.adapter = adapter
            }
        }

        MovieResponse.getMovieKeywords(id!!.toInt()) { keywords: List<Keywords> ->
            run {
                var kw = ""
                for (element in keywords) {
                    kw += element.name + ", "
                }
                if(kw.isNotEmpty()) keyword.text = kw
                else keyword.text = "No keywords"
            }
        }

        MovieResponse.getMovieSimilar(id!!.toInt(), page) { movieList ->
            if(movieList.isEmpty()){
                similarRec.visibility = View.GONE
                similarTitle.visibility = View.GONE
            }
            Similar_list = ArrayList()
            Similar_list = movieList as MutableList<Movies>
            val adapter = MovieAdapter(movieList as MutableList<Movies>)
            similarRec.adapter = adapter
            similarRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollHorizontally(1)) {
                        MovieResponse.getMovieSimilar(id!!.toInt(), page + 1) {
                            val movies = it
                            adapter.addData(movies)
                        }
                    }
                }
            })
            adapter.setOnItemClickListener(object :
                MovieAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, imageView: CardView) {
                    val intent =
                        Intent(applicationContext, DetailActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@DetailActivity as Activity, imageView, "image")
                    intent.putExtra("id", movieList[position].id)
                    intent.putExtra("isMovie", true)
                    intent.putExtra("title", movieList[position].title)
                    startActivity(intent, options.toBundle())
                }
            })
        }

        MovieResponse.getMovieRecommendations(id!!.toInt(), page) { movieList ->
            if(movieList.isEmpty()){
                recommendRec.visibility = View.GONE
                recommendedTitle.visibility = View.GONE
            }
            val adapter = MovieAdapter(movieList as MutableList<Movies>)
            recommendRec.adapter = adapter
            recommendRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollHorizontally(1)) {
                        MovieResponse.getMovieRecommendations(id!!.toInt(), page + 1) {
                            val movies = it
                            adapter.addData(movies)
                        }
                    }
                }
            })
            adapter.setOnItemClickListener(object :
                MovieAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, imageView: CardView) {
                    val intent =
                        Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra("id", movieList[position].id)
                    intent.putExtra("title", movieList[position].title)
                    intent.putExtra("isMovie", true)
                    startActivity(intent)
                }
            })
        }

        MovieResponse.discoverMovies(page) { movies: List<Movies> ->
            run {
                val adapter = MovieAdapter(movies as MutableList<Movies>)
                discoverRec.adapter = adapter
                discoverRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollHorizontally(1)) {
                            MovieResponse.discoverMovies(page + 1) {
                                val moviesList = it
                                adapter.addData(moviesList)
                            }
                        }
                    }
                })
                adapter.setOnItemClickListener(object :
                    MovieAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int, imageView: CardView) {
                        val intent = Intent(applicationContext, DetailActivity::class.java)
                        intent.putExtra("id", movies[position].id)
                        intent.putExtra("isMovie", true)
                        intent.putExtra("title", movies[position].title)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun doDatabaseStuff(database: RoomDatabase, favBtn: Button, fav: Boolean) {
        if (database is MovieDatabase) {
            val movieDao = database.movieDAO()
            var movie: Movies? = null
            MovieResponse.getMovieDetail(id!!.toInt()) { movieDetail ->
                run {
                    if(movieDetail.posterPath!=null && movieDetail.backdropPath!=null) {
                        movie = Movies(
                            movieDetail.id.toString(),
                            movieDetail.title,
                            movieDetail.voteCount.toString(),
                            movieDetail.overview,
                            movieDetail.posterPath.toString(),
                            movieDetail.releaseDate,
                            movieDetail.voteAverage.toString()

                        )
                    }
                    else{
                        movie = Movies(
                            movieDetail.id.toString(),
                            movieDetail.title,
                            movieDetail.voteCount.toString(),
                            movieDetail.overview,
                            "https://www.themoviedb.org/assets/2/v4/logos/256x256-dark-bg-01a111196ed89d33f7e41c03f6f34ca37ac3abf7f7f63f7b2f8f7e7e042c1f7d.png",
                            movieDetail.releaseDate,
                            movieDetail.voteAverage.toString()

                        )
                    }
                }
            }
            database.movieDAO().getMovies().observe(this as LifecycleOwner) {
                for (element in it) {
                    if (element.id == id) {
                        favBtn.text = BTN_TEXT_REMOVE
                    }
                }
            }
            if (fav) {
                favBtn.text = BTN_TEXT_REMOVE
                favBtn.setOnClickListener {
                    movieDao.deleteMovie(movie!!)
                    favBtn.text = BTN_TEXT_ADD
                    favBtn.setOnClickListener {
                        movieDao.insertMovie(movie!!)
                        favBtn.text = BTN_TEXT_REMOVE
                    }
                }
            } else {
                favBtn.text = BTN_TEXT_ADD
                favBtn.setOnClickListener {
                    movieDao.insertMovie(movie!!)
                    favBtn.text = BTN_TEXT_REMOVE
                    favBtn.setOnClickListener {
                        movieDao.deleteMovie(movie!!)
                        favBtn.text = BTN_TEXT_ADD
                    }
                }
            }
        } else if (database is TvDatabase) {
            val tvDao = database.tvDAO()
            var tv: TV? = null
            TvResponse.getTvDetails(id!!.toInt()) { tvDetail ->
                run {
                    tv = TV(
                        tvDetail.id.toString(),
                        tvDetail.name,
                        tvDetail.voteCount.toString(),
                        tvDetail.overview,
                        tvDetail.posterPath.toString(),
                        tvDetail.firstAirDate,
                        tvDetail.voteAverage.toString(),
                    )
                }
            }
            if (fav) {
                favBtn.text = BTN_TEXT_REMOVE
                favBtn.setOnClickListener {
                    tvDao.deleteTvShow(tv!!)
                    favBtn.text = BTN_TEXT_ADD
                    favBtn.setOnClickListener {
                        tvDao.insertTvShow(tv!!)
                        favBtn.text = BTN_TEXT_REMOVE
                    }
                }
            } else {
                favBtn.text = BTN_TEXT_ADD
                favBtn.setOnClickListener {
                    tvDao.insertTvShow(tv!!)
                    favBtn.text = BTN_TEXT_REMOVE
                    favBtn.setOnClickListener {
                        tvDao.deleteTvShow(tv!!)
                        favBtn.text = BTN_TEXT_ADD
                    }
                }
            }
        }
    }

    private fun getTrailers(videos: List<Videos>) {
        val size = videos.size - 1
        for (i in 0..size) {
            val data = videos[i]
            yT.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                @SuppressLint("SetTextI18n")
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    if (data.site == "YouTube") {
                        val videoId = data.key
                        site.text = data.site
                        if (data.official) {
                            official.text = "Official"
                        } else {
                            official.text = "Unofficial"
                        }
                        youTubePlayer.cueVideo(videoId!!, 0.00F)
                    } else {
                        site.text = data.site
                        if (data.official) {
                            official.text = "Official"
                        } else {
                            official.text = "Unofficial"
                        }
                    }
                }
            })
            yT.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                                val layoutParams = yT.layoutParams
//                                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//                                yT.layoutParams = layoutParams
//
//                                // Hide the status bar
//                                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//                                actionBar?.hide()
                }

                override fun onYouTubePlayerExitFullScreen() {
                    val layoutParams = yT.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = 0 // Set the height to 0 to use the aspect ratio
                    yT.layoutParams = layoutParams

                    // Show the status bar
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    actionBar?.show()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getMovieDetails(id: Int) {
        MovieResponse.getMovieDetail(id) {
            if(it.posterPath != null && it.backdropPath != null || it.posterPath != "" && it.backdropPath != ""){
                setData(
                    it.title,
                    it.overview,
                    it.releaseDate,
                    it.popularity.toString(),
                    it.voteAverage.toString(),
                    it.posterPath,
                    it.backdropPath
                )
            }
            else {
                setData(
                    it.title,
                    it.overview,
                    it.releaseDate,
                    it.popularity.toString(),
                    it.voteAverage.toString(),
                    "https://www.themoviedb.org/assets/2/v4/logos/256x256-dark-bg-01a111196ed89d33f7e41c03f6f34ca37ac3abf7f7f63f7b2f8f7e7e042c1f7d.png",
                    "https://www.themoviedb.org/assets/2/v4/logos/256x256-dark-bg-01a111196ed89d33f7e41c03f6f34ca37ac3abf7f7f63f7b2f8f7e7e042c1f7d.png"
                )
            }
        }
    }

    private fun getTvDetails(id: Int) {
        relativeLayout.visibility = View.GONE
        TvResponse.getTvDetails(id) {
            if(it.posterPath != null  && it.backdropPath != null ){
                setData(
                    it.name,
                    it.overview,
                    it.firstAirDate,
                    it.popularity.toString(),
                    it.voteAverage.toString(),
                    it.posterPath.toString(),
                    it.backdropPath.toString()
                )
            }
            else{
                setData(
                    it.name,
                    it.overview,
                    it.firstAirDate,
                    it.popularity.toString(),
                    it.voteAverage.toString(),
                    "https://www.themoviedb.org/assets/2/v4/logos/256x256-dark-bg-01a111196ed89d33f7e41c03f6f34ca37ac3abf7f7f63f7b2f8f7e7e042c1f7d.png",
                    "https://www.themoviedb.org/assets/2/v4/logos/256x256-dark-bg-01a111196ed89d33f7e41c03f6f34ca37ac3abf7f7f63f7b2f8f7e7e042c1f7d.png"
                )
            }
        }
    }

    private fun getAnimeDetails(id: Int) {
        relativeLayout.visibility = View.GONE
        detailLayout.visibility = View.GONE
        supportActionBar!!.title = intent.getStringExtra("title")
        supportFragmentManager.beginTransaction().apply {
            val fragment = AnimeDetail()
            val bundle = Bundle()
            bundle.putString("id", id.toString())
            bundle.putBoolean("fav", fav)
            fragment.arguments = bundle
            replace(R.id.detail_frame, fragment)
            commit()
        }
    }

    private fun getTorrentLinks() {
        detailProgress.visibility = View.VISIBLE
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Quality")
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val list = ArrayList<YtsLinkModel>()
        MovieResponse.getMovieDetail(id!!.toInt()) { movie ->
            list.clear()
            YtsResponse.getMovieDetail(movie.imdbId) { movieDetails ->
                if(movieDetails.status == "ok"){
                    detailProgress.visibility = View.GONE
                }
                val json = JSONObject(Gson().toJson(movieDetails))
                val movieJson = json.getJSONObject("data").getJSONObject("movie")
                if (movieJson.has("torrents")) {
                    val torrents = movieDetails.data.movie.torrents
                    for (torrent in torrents) {
                        list.add(
                            YtsLinkModel(
                                torrent.quality,
                                torrent.type,
                                torrent.size,
                                torrent.hash
                            )
                        )
                    }
                    val adapter = TorrentLinkAdapter(list)
                    recyclerView.adapter = adapter
                    builder.setPositiveButton(null, null)
                    builder.setNegativeButton(null, null)
                    builder.setMessage("Torrents found")
                    builder.setView(recyclerView)
                    if (list.isNotEmpty()) {

                    } else {
                        builder.setMessage("No torrents found")
                        builder.setView(null)
                    }
                    val dialog = builder.create()
                    dialog.show()
                    adapter.setOnItemClickListener(object : TorrentLinkAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val hash = list[position].hash
                            if(isStream) {
                                val intent = Intent(this@DetailActivity, StreamActivity::class.java)
                                intent.putExtra("hash", hash)
                                intent.putExtra("imdb", movie.imdbId)
                                intent.putExtra("id", id)
                                intent.putExtra("slug", movieDetails.data.movie.slug)
                                intent.putExtra("size", list[position].size)
                                startActivity(intent)
                                dialog.dismiss()
                                this@DetailActivity.finish()
                            }
                            else {
                                dialog.dismiss()
                                openBuilder(hash, movieDetails.data.movie.slug)
                            }
                        }
                    })
                }else{
                    builder.setMessage("No torrents found")
                    builder.setView(null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun openBuilder(hash: String, slug: String){
        val builder = AlertDialog.Builder(this)
//        val recyclerView = RecyclerView(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.builder_background, null)
        builder.setView(view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.builder_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = DownloadAdapter(listOf(DownloadItem("Open link"), DownloadItem("Copy Link")))
        recyclerView.adapter = adapter
        builder.setPositiveButton(null, null)
        builder.setNegativeButton(null, null)
//        builder.setView(recyclerView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_builder)
        dialog.show()
        val magnetUrl = "magnet:?xt=urn:btih:$hash&dn=$slug&tr=udp%3A%2F%2Fopen.stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.tiny-vps.com%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.torrent.eu.org%3A451%2Fannounce&tr=udp%3A%2F%2Fexplodie.org%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.cyberia.is%3A6969%2Fannounce&tr=udp%3A%2F%2Fipv4.tracker.harry.lu%3A80%2Fannounce&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.birkenwald.de%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.moeking.me%3A6969%2Fannounce&tr=udp%3A%2F%2Fopentor.org%3A2710%2Fannounce&tr=udp%3A%2F%2Ftracker.dler.org%3A6969%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2970%2Fannounce&tr=https%3A%2F%2Ftracker.foreverpirates.co%3A443%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=http%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Fopentracker.i2p.rocks%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.internetwarriors.net%3A1337%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969%2Fannounce&tr=udp%3A%2F%2Fcoppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.zer0day.to%3A1337%2Fannounce"
        adapter.setOnItemClickListener(object : DownloadAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if(position == 0){
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            magnetUrl
                        ))
                    this@DetailActivity.startActivity(intent)
                    dialog.dismiss()

                }
                else if(position == 1){
                    Log.d("TAG", "onItemClick: $hash")
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", magnetUrl)
                    clipboard.setPrimaryClip(clip)
                    dialog.dismiss()
                    Toast.makeText(this@DetailActivity, "Magnet link copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setData(
        title: String,
        overview: String,
        date: String,
        voteCount: String,
        score: String,
        image: String?,
        backdrop: String?,
    ) {
        supportActionBar!!.title = title
        title1.text = title
        overview1.text = overview
        date1.text = date
        val voteCountInt = voteCount.toDouble().toInt(
        )
        votes1.text = voteCountInt.toString()
        val scoreRound = String.format("%.1f", score.toDouble())
        rating.text = "$scoreRound/10"
        if (image == null) {
            Glide.with(this).load(R.drawable.profile).into(detailImage)
        } else {
            if (image.contains("https")) {
                Glide.with(this).load(image).into(detailImage)
            }
            else {
//                Glide.with(this).load(IMAGE_BASE + image).into(detailImage)
                Glide.with(this).load(IMAGE_BASE + backdrop).into(backdropImage)
                Picasso.get().load(IMAGE_BASE + image).into(object: com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?, from: Picasso.LoadedFrom?) {
                        Palette.from(bitmap!!).generate { palette ->
                            val defaultColor = 0x000000
                            val color = palette?.getVibrantColor(defaultColor) ?: defaultColor
                            val gradientDrawable = GradientDrawable(
                                GradientDrawable.Orientation.TL_BR,
                                intArrayOf(color, Color.BLACK, Color.BLACK)
                            )
                            gradientDrawable.cornerRadius = 100f
                            detailContent.background = gradientDrawable
                            detailContent2.background = gradientDrawable
                        }
                    }
                    override fun onBitmapFailed(e: Exception?, errorDrawable: android.graphics.drawable.Drawable?) {
                        Log.d("TAG", "bindMovie: ${e?.message}")
                    }
                    override fun onPrepareLoad(placeHolderDrawable: android.graphics.drawable.Drawable?) {
                    }
                })

                Picasso.get()
                    .load(IMAGE_BASE + image)
                    .noFade()
                    .into(detailImage, object : Callback {
                        override fun onSuccess() {
                            supportStartPostponedEnterTransition()
                        }

                        override fun onError(e: java.lang.Exception?) {
                            supportStartPostponedEnterTransition()
                        }
                    })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }


    override fun onDestroy() {
        super.onDestroy()
        yT.release();
    }

    override fun onResume() {
        super.onResume()
    }
}