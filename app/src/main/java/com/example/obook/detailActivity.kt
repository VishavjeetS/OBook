package com.example.obook

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.obook.Model.Movie
import com.example.obook.Model.User
import com.example.obook.util.Constant
import com.example.obook.util.Popular
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class detailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val url = Uri.parse(intent.getStringExtra("image"))
        val title = intent.getStringExtra("title")
        val overview = intent.getStringExtra("overview")
        val date = intent.getStringExtra("date")
        val vote_count = intent.getStringExtra("popularity")

        Glide.with(applicationContext).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview
        date1.text = date
        votes1.text = vote_count

        val instance = User().getInstance()
        val localList:ArrayList<Movie> = arrayListOf()

        fav_btn.setOnClickListener {
            if(Constant().USERSIGNIN == "0"){
                fav_btn.error = "Please Sign in"
            }
            else{
                localList.add(Movie(title!!, url))
                Log.d("list", localList.toString())
                Log.d("list", localList.size.toString())
            }
        }
        instance.setFavList(localList)
        Log.d("list1", instance.getFavList().size.toString())
    }
}