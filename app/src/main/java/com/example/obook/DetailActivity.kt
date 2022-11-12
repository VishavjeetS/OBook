package com.example.obook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.obook.Fragments.Favourite
import com.example.obook.Model.Movies
import com.example.obook.Model.User
import com.example.obook.Room.MovieDatabase
import com.example.obook.util.Constant
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {
    lateinit var database: MovieDatabase
    private var BTN_TEXT_ADD = "Add to favourite"
    private var BTN_TEXT_REMOVE = "Remove from favorites"
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val url = Uri.parse(intent.getStringExtra("image"))
        val title = intent.getStringExtra("title")
        val id = intent.getStringExtra("movieId")
        val overview = intent.getStringExtra("overview")
        val date = intent.getStringExtra("date")
        val voteCount = intent.getStringExtra("popularity")
        val fav = intent.getBooleanExtra("fav", false)
        val movie = Movies(id!!, title, voteCount, overview, url.toString(), date!!)

        val fav_btn = findViewById<Button>(R.id.fav_btn)

//        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().build()
        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movieDB").fallbackToDestructiveMigration().allowMainThreadQueries().build()

        Glide.with(this).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview
        date1.text = date
        votes1.text = voteCount

        val obj = Constant()
        Log.d("True Value ", obj.getInfo().toString())
        if(obj.getInfo()){
            fav_btn.text = "Please Sign in"
            fav_btn.isClickable = false
        }

        if (fav){
            fav_btn.text = BTN_TEXT_REMOVE
        }

        database.movieDAO().getMovies().observe(this as LifecycleOwner){
            if(it.contains(movie)){
                fav_btn.text = BTN_TEXT_REMOVE
            }
        }

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
    }
}