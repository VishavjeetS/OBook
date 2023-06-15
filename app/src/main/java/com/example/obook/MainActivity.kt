package com.example.obook

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.adapter.tmdb.MovieAdapter
import com.example.obook.adapter.tmdb.TvAdapter
import com.example.obook.adapter.anime.AnimeAdapter
import com.example.obook.authentication.Login
import com.example.obook.fragments.*
import com.example.obook.model.MovieModel.MovieResponse
import com.example.obook.model.MovieModel.Responses.Movies
import com.example.obook.model.TvModel.Responses.TV
import com.example.obook.model.TvModel.TvResponse
import com.example.obook.model.UserModel.User
import com.example.obook.model.animeModel.Manga.mangadex.response.mangaResponse.MangaResponse
import com.example.obook.model.animeModel.anime.topAnime.Anime
import com.example.obook.services.manga.MangaApiInterface
import com.example.obook.services.manga.MangaApiService
import com.example.obook.services.yts.YtsResponse
import com.example.obook.util.Constant
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    lateinit var searchView: SearchView
    private var obj = Constant.getInstance()
    private var userSign = false
    private var gSign = false
    var page = 1
    lateinit var searchAdapter: MovieAdapter
    lateinit var searchRV: RecyclerView
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mangaRv: RecyclerView
    private val REQUEST_WRITE_PERMISSION = 786
    private val REQUEST_READ_PERMISSION = 787

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        mangaRv = findViewById(R.id.mangaRV)
        mangaRv.layoutManager = LinearLayoutManager(this)
        mangaRv.setHasFixedSize(true)
        //getChapters()

        //watchMovie()

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = null

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)

        userSign = intent.getBooleanExtra("Not Sign", false)
        gSign = intent.getBooleanExtra("gSign", false)
        obj.setInfo(userSign)
        obj.setGSign(gSign)

        println("True Value: $gSign")
        println("True Value: " + obj.getGSign())
        searchRV = findViewById(R.id.searchRV)
        searchRV.setHasFixedSize(true)

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = nav_view.getHeaderView(0)
        val username = header.findViewById<TextView>(R.id.username)
        val email1 = header.findViewById<TextView>(R.id.email)
        val img = header.findViewById<ImageView>(R.id.ProfImg)

        val constObj = Constant.getInstance()
        if (constObj.getGSIGN_NAME()) {
            val name = intent.getStringExtra("name")
            toolbarText.text = "Hey, $name"
        }
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.movie -> {
                    constObj.setTv(false)
                    constObj.setScreen("Movie")
                    makeCurrentScreen(Popular())
                    nav_bar.visibility = View.VISIBLE
                    drawer.close()
                }
                R.id.anime -> {
                    constObj.setScreen("Anime")
                    makeCurrentScreen(Popular())
                    nav_bar.visibility = View.VISIBLE
                    drawer.close()
                }
                R.id.tv -> {
                    constObj.setTv(true)
                    constObj.setScreen("TV")
//                    makeCurrentScreen(Popular())
                    nav_bar.visibility = View.VISIBLE
                    drawer.close()
                }
                else -> {
                    constObj.setScreen("Manga")
                    makeCurrentScreen(Popular())
                    nav_bar.visibility = View.GONE
                    drawer.close()
                }
            }
            true
        }

        when (constObj.getScreen()) {
            "Movie" -> {
                constObj.setScreen("Movie")
                makeCurrentScreen(Popular())
                nav_bar.visibility = View.VISIBLE
            }
            "TV" -> {
                constObj.setScreen("TV")
                makeCurrentScreen(Popular())
                nav_bar.visibility = View.VISIBLE
            }
            "Anime" -> {
                constObj.setScreen("Anime")
                makeCurrentScreen(Popular())
                nav_bar.visibility = View.VISIBLE
            }
            "Manga" -> {
                constObj.setScreen("Manga")
                makeCurrentScreen(Popular())
                nav_bar.visibility = View.GONE
            }

            else -> {
                constObj.setScreen("Movie")
                makeCurrentScreen(Popular())
                nav_bar.visibility = View.VISIBLE
            }
        }

        val fUser = Firebase.auth.currentUser
        fUser?.let {
            for (profile in it.providerData) {
                //(ex: google)
                constObj.setProviderId(profile.providerId)
                val uid = profile.uid
                if (constObj.getProviderId() == "password") {
                    val firebaseUid = fUser.uid
                    val refUsers =
                        FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUid)
                    refUsers.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val userObj = User.getInstance()
                                val MUser: User? = snapshot.getValue(User::class.java)
                                toolbarText.text = "Hey, ${MUser?.getName()}"
                                username.text = MUser?.getName()
                                email1.text = MUser?.getEmail()
                                userObj.setName(MUser?.getName().toString())
                                userObj.setUID(MUser?.getUID().toString())
                                userObj.setEmail(MUser?.getEmail().toString())
                                Log.d("UserObj - Password", "${userObj.getName()}")
                                toolbarText.setTextColor(Color.WHITE)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                } else {
                    constObj.setProviderId(profile.providerId)
                    val userObj = User.getInstance()
                    fUser.reload()
                    userObj.setName(profile.displayName.toString())
                    Log.d("Name", userObj.getName().toString())
                    userObj.setEmail(profile.email.toString())
                    userObj.setUri(profile.photoUrl.toString())
                    userObj.setUID(profile.uid)
                    toolbarText.text = "Hey, ${userObj.getName()}"
                    username.text = userObj.getName()
                    email1.text = userObj.getEmail()
                    Glide.with(this).load(Uri.parse(userObj.getUri())).into(img);
                    Log.d(
                        "UserObj ",
                        "${constObj.getProviderId()} ${userObj.getName()} ${userObj.getUID()} "
                    )
                    toolbarText.setTextColor(Color.WHITE)
                }
            }
            val userObj = User.getInstance()
            Log.d("GSignIn ", constObj.getProviderId())
            Log.d(
                "UserObj ",
                "${constObj.getProviderId()} ${userObj.getName()} ${userObj.getUID()} "
            )
        }

        val popular = Popular()
        val favourite = Favourite()
        val topRated = TopRated()
        makeCurrentScreen(popular)

        nav_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.popular -> {
                    makeCurrentScreen(popular)
                }
                R.id.topRated -> {
                    makeCurrentScreen(topRated)
                }
                R.id.favorite -> {
                    makeCurrentScreen(favourite)
                }
            }
            true
        }
    }


    private fun makeCurrentScreen(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper_frame, fragment)
            commit()
        }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        searchView = menu.findItem(R.id.Search)?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        if (obj.getInfo()) {
            val register: MenuItem? = menu.findItem(R.id.logout_item)
            register?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            if (newText.isNotEmpty()) {
                searchMovies(newText)
            } else {
                searchRV.visibility = View.GONE
            }
        }
        return true
    }

    private fun searchMovies(query: String) {
        when (Constant.getInstance().getScreen()) {
            "Movie" -> {
                MovieResponse.getMovieSearch(query, page) {
                    val movies = it.movies
                    searchRV.visibility = View.VISIBLE
                    searchAdapter = MovieAdapter(movies as MutableList<Movies>)
                    searchRV.adapter = AlphaInAnimationAdapter(searchAdapter)
                    searchRV.layoutManager = GridLayoutManager(applicationContext, 2)
                    searchAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int, imageView: CardView) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra("id", movies[position].id)
                            intent.putExtra("isMovie", true)
                            intent.putExtra("title", movies[position].title.toString())
                            startActivity(intent)
                        }
                    })
                }

            }
            "TV" -> {
                TvResponse.getTvSearch(query) {
                    val tv = it
                    searchRV.visibility = View.VISIBLE
                    val searchAdapter = TvAdapter(tv as MutableList<TV>)
                    searchRV.adapter = AlphaInAnimationAdapter(searchAdapter)
                    searchRV.layoutManager = GridLayoutManager(applicationContext, 2)
                    searchAdapter.setOnItemClickListener(object : TvAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra("id", tv[position].id)
                            intent.putExtra("isTv", true)
                            intent.putExtra("title", tv[position].title.toString())
                            startActivity(intent)
                        }
                    })
                }

            }
            else -> {
                Anime.getSearchAnime(query) {
                    searchRV.visibility = View.VISIBLE
                    val searchAdapter = AnimeAdapter(it)
                    searchRV.adapter = AlphaInAnimationAdapter(searchAdapter)
                    searchRV.layoutManager = GridLayoutManager(applicationContext, 2)
                    searchAdapter.setOnItemClickListener(object : AnimeAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra("id", it[position].malId.toString())
                            intent.putExtra("anime", true)
                            intent.putExtra("title", it[position].title.toString())
                            startActivity(intent)
                        }

                    })
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT)
                .show()
            R.id.logout_item -> {
                if (isGSign()) {
                    googleSignOut()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(applicationContext, Login::class.java)
                    Toast.makeText(applicationContext, "Log out", Toast.LENGTH_SHORT).show()
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
            R.id.settings -> makeCurrentScreen(SettingsFragment())
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun isGSign(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    private fun googleSignOut() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_server_client))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        mGoogleSignInClient.signOut()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun getChapters() {
        val mangaApiService = MangaApiService.getInstance().create(MangaApiInterface::class.java)

        MangaResponse.getMangaChapters("553e781b-9b27-4520-aa6a-a98c7d749c9f") { it ->
            val volumes = it.volumes.map { volume -> volume.value }.reversed().toList()
            val chapters = volumes[0].chapters.map { chapter -> chapter.value }.reversed().toList()
            val chapter = chapters[0]
            Log.d("volumes", chapter.toString())
        }

//        mangaApiService.getChapterImages("39767d5b-f925-4ca3-9c0d-bc889ec50c45")!!.enqueue(object: Callback<ChapterImagesResponse?>{
//            override fun onResponse(call: Call<ChapterImagesResponse?>, response: Response<ChapterImagesResponse?>) {
//                if (response.isSuccessful) {
//                    val chapterImagesResponse = response.body()!!
//                    val images = chapterImagesResponse.chapter.data
//                    val hash = chapterImagesResponse.chapter.hash
//                    val mangaAdapter = MangaImageAdapter(images, hash)
//                    mangaRv.adapter = mangaAdapter
//
//                    Log.d("response images", chapterImagesResponse.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<ChapterImagesResponse?>, t: Throwable) {
//                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        mangaApiService.getChapter("39767d5b-f925-4ca3-9c0d-bc889ec50c45")!!.enqueue(object: Callback<ChapterResponse?>{
//            override fun onResponse(call: Call<ChapterResponse?>, response: Response<ChapterResponse?>) {
//                if (response.isSuccessful) {
//                    val chapterResponse = response.body()!!.data
//                    Log.d("response", chapterResponse.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<ChapterResponse?>, t: Throwable) {
//                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    private fun watchMovie() {
        val imdbId = "tt9114286" // replace with your IMDb ID
        YtsResponse.getMovieDetail(imdbId){ movieDetails ->

            // Extract the magnet URI or streaming URLs from the movieDetails object
            val magnetUri = movieDetails.data.movie.torrents[0].url ?: ""
            Log.d("magnet", magnetUri)
        }


    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "OBook")
        )
        val extractorsFactory = DefaultExtractorsFactory()
        return ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory)
            .createMediaSource(MediaItem.fromUri(uri))
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        }
    }
}