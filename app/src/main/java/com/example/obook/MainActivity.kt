package com.example.obook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Adapter.TvAdapter
import com.example.obook.Authentication.Login
import com.example.obook.Fragments.*
import com.example.obook.Model.MovieModel.MovieResponse
import com.example.obook.Model.MovieModel.Movies
import com.example.obook.Model.TvModel.TV
import com.example.obook.Model.TvModel.TvResponse
import com.example.obook.Model.UserModel.User
import com.example.obook.services.Movie.MovieApiInterface
import com.example.obook.services.Movie.MovieApiService
import com.example.obook.services.TVShows.TvApiInterface
import com.example.obook.services.TVShows.TvApiService
import com.example.obook.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


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
    private lateinit var searchInfo: ArrayList<Movies>
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.movie -> {
                    constObj.setTv(false)
                    constObj.setScreen("Movie")
                    makeCurrentScreen(Popular())
                }
                R.id.anime -> {
//                    constObj.setScreen("Anime")
                    makeCurrentScreen(AnimeFragment())
                }
                R.id.tv -> {
                    constObj.setTv(true)
                    constObj.setScreen("TV")
                    makeCurrentScreen(Popular())
                }
                else -> {
                    constObj.setScreen("Movie")
                    makeCurrentScreen(Popular())
                }
            }
            true
        }

        constObj.setScreen("Movie")
        makeCurrentScreen(Popular())

        val FUser = Firebase.auth.currentUser
        FUser?.let {
            for (profile in it.providerData) {
                //(ex: google)
                val constObj = Constant.getInstance()
                constObj.setProviderId(profile.providerId)
                val uid = profile.uid
                if(constObj.getProviderId() == "password"){
                    val firebaseUid = FUser.uid
                    val refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUid)
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
                }
                else{
                    val constObj = Constant.getInstance()
                    constObj.setProviderId(profile.providerId)
                    val userObj = User.getInstance()
                    FUser.reload()
                    userObj.setName(profile.displayName.toString())
                    Log.d("Name", userObj.getName().toString())
                    userObj.setEmail(profile.email.toString())
                    userObj.setUri(profile.photoUrl.toString())
                    userObj.setUID(profile.uid)
                    toolbarText.text = "Hey, ${userObj.getName()}"
                    username.text = userObj.getName()
                    email1.text = userObj.getEmail()
                    Glide.with(this).load(Uri.parse(userObj.getUri())).into(img);
                    Log.d("UserObj ", "${constObj.getProviderId()} ${userObj.getName()} ${userObj.getUID()} ")
                    toolbarText.setTextColor(Color.WHITE)
                }
            }
            val const = Constant.getInstance()
            val userObj = User.getInstance()
            Log.d("GSignIn ", const.getProviderId())
            Log.d("UserObj ", "${const.getProviderId()} ${userObj.getName()} ${userObj.getUID()} ")
        }

        val popular = Popular()
        val favourite = Favourite()
        val topRated = TopRated()
        makeCurrentScreen(popular)

        nav_bar.setOnNavigationItemSelectedListener {
            when(it.itemId){
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

    private fun makeCurrentScreen(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.wrapper_frame, fragment)
        commit()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        else {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        searchView = menu.findItem(R.id.Search)?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        if(obj.getInfo()){
            val register: MenuItem? = menu.findItem(R.id.logout_item)
            register?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            if(newText.isNotEmpty()){
                searchMovies(newText)
            }
            else{
                searchRV.visibility = View.GONE
            }
        }
        return true
    }

    private fun searchMovies(movie: String) {
        when(Constant.getInstance().getScreen()){
            "Movie" -> {
                MovieApiService.getInstance().create(MovieApiInterface::class.java)
                    .searchMovie(movie, page).enqueue(object :Callback<MovieResponse>{
                        override fun onResponse(
                            call: Call<MovieResponse>,
                            response: Response<MovieResponse>,
                        ) {
                            val movies = response.body()!!.movies
                            Log.d("Search", movies.toString())
                            searchRV.visibility = View.VISIBLE
                            searchAdapter = MovieAdapter(movies as MutableList<Movies>)
                            searchRV.adapter = AlphaInAnimationAdapter(searchAdapter)
                            searchRV.layoutManager = GridLayoutManager(applicationContext, 2)
                            searchAdapter.setOnItemClickListener(object: MovieAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
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

                        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }
            "TV"  -> {
                TvApiService.getInstance().create(TvApiInterface::class.java)
                    .getTvSearch(movie).enqueue(object :Callback<TvResponse>{
                        override fun onResponse(
                            call: Call<TvResponse>,
                            response: Response<TvResponse>,
                        ) {
                            val movies = response.body()!!.result
                            Log.d("Search", movies.toString())
                            searchRV.visibility = View.VISIBLE
                            val searchAdapter = TvAdapter(movies as MutableList<TV>)
                            searchRV.adapter = AlphaInAnimationAdapter(searchAdapter)
                            searchRV.layoutManager = GridLayoutManager(applicationContext, 2)
                            searchAdapter.setOnItemClickListener(object: TvAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
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

                        override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        when (item.itemId) {
            R.id.help_item -> Toast.makeText(applicationContext, "Help", Toast.LENGTH_SHORT)
                .show()
            R.id.logout_item -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, Login::class.java))
                Toast.makeText(applicationContext,"Log out", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            R.id.settings -> makeCurrentScreen(SettingsFragment())
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}