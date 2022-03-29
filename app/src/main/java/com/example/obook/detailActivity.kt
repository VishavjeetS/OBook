package com.example.obook

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class detailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val url = Uri.parse(intent.getStringExtra("image"))
        val title = intent.getStringExtra("title");
        val overview = intent.getStringExtra("overview");


        Glide.with(applicationContext).load(url).into(detailImage)
        title1.text = title
        overview1.text = overview

    }
}