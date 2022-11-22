package com.example.obook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.obook.Authentication.Login
import com.example.obook.Authentication.SignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class Welcome : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler().postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            this.finish()
        }, 3000)

        val img = findViewById<ImageView>(R.id.imgView)
        val glide = DrawableImageViewTarget(img)
        Glide.with(this).load(R.drawable.obook1).into(glide)
    }
}