package com.example.obook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        getSupportActionBar()?.hide();
    }
    fun Login(view: android.view.View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
    fun register(view: android.view.View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }
}