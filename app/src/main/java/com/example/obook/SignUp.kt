package com.example.obook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.sign
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edpass as edpass1

class SignUp : AppCompatActivity() {
    var name : String = ""
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

        mAuth = FirebaseAuth.getInstance()

        sign.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        name = edname.text.toString()
        val email:String = email.text.toString()
        val password:String = edpass.text.toString()

        if(name.equals("")){
            Toast.makeText(applicationContext, "Please write username", Toast.LENGTH_SHORT).show()
        }
        else if (email.equals("")){
            Toast.makeText(applicationContext, "Please write email", Toast.LENGTH_SHORT).show()
        }
        else if (password.equals("")){
            Toast.makeText(applicationContext, "Please write password", Toast.LENGTH_SHORT).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                    val userhashMap =HashMap<String, Any>()
                    userhashMap["uid"] = firebaseUserID
                    userhashMap["name"] = name
                    userhashMap["email"] = email

                    refUsers.updateChildren(userhashMap)
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

    fun signin(view: android.view.View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}