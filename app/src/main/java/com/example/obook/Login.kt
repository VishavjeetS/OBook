package com.example.obook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edpass
import kotlinx.android.synthetic.main.activity_login.sign
import kotlinx.android.synthetic.main.activity_sign_up.*

class Login : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar:Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(applicationContext, Welcome::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        sign.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = edemail.getText().toString()
        val password: String = edpass.getText().toString()
        if (email.equals("")){
            Toast.makeText(applicationContext, "Please write email", Toast.LENGTH_LONG).show()
        }
        else if (password.equals("")){
            Toast.makeText(applicationContext, "Please write password", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(applicationContext, "Error Message: "+task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun signUp(view: android.view.View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }
    fun forpass(view: android.view.View) {

    }
    fun signIn(view: android.view.View) {}
}