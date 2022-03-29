package com.example.obook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.obook.util.SignUp
import com.google.firebase.auth.FirebaseAuth

class Welcome : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        getSupportActionBar()?.hide();
    }
    fun Login(view: android.view.View) {
        val intent = Intent(this, com.example.obook.util.Login::class.java)
        startActivity(intent)
        this.finish()
    }
    fun register(view: android.view.View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        if(p0.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

}