package com.example.obook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.obook.Authentication.SignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class Welcome : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide();
    }
    fun Login(view: android.view.View) {
        val intent = Intent(this, com.example.obook.Authentication.Login::class.java)
        startActivity(intent)
        this.finish()
    }
    fun register(view: android.view.View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
//        if(p0.currentUser!=null){
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

}