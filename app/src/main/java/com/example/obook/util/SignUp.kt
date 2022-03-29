package com.example.obook.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.obook.MainActivity
import com.example.obook.R
import com.example.obook.Welcome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.sign
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    var name : String = ""
    lateinit var eDpassword:EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val toolbar:Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(applicationContext, Welcome::class.java)
            startActivity(intent)
            finish()
        }
        eDpassword = findViewById(R.id.edpass)
        mAuth = FirebaseAuth.getInstance()

        sign.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        name = edname.text.toString()
        val email:String = email.text.toString()
        val password:String = eDpassword.toString()

        when {
            name == "" -> {
                Toast.makeText(applicationContext, "Please write username", Toast.LENGTH_SHORT).show()
            }
            email == "" -> {
                Toast.makeText(applicationContext, "Please write email", Toast.LENGTH_SHORT).show()
            }
            password == "" -> {
                Toast.makeText(applicationContext, "Please write password", Toast.LENGTH_SHORT).show()
            }
            else -> {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val usersMap =HashMap<String, Any>()
                        usersMap["uid"] = firebaseUserID
                        usersMap["name"] = name
                        usersMap["email"] = email

                        refUsers.updateChildren(usersMap)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    }
                    else{
                        Toast.makeText(applicationContext, "Error Message: "+task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun signin(view: android.view.View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}