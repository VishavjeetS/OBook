package com.example.obook

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_pass_change.*
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import com.example.obook.Fragments.SettingsFragment

import com.google.android.gms.tasks.OnFailureListener

class passChange : AppCompatActivity() {
    lateinit var newPass:EditText
    lateinit var confirmPass:EditText
    var firebaseUser: FirebaseUser? = null
    lateinit var alertDialog:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_change)
        supportActionBar?.hide()

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.back)


        newPass = findViewById(R.id.new_pass)
        confirmPass = findViewById(R.id.confirm_pass)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun updatePass(view: android.view.View) {
        if(newPass.text.toString().isNotEmpty() && confirmPass.text.toString().isNotEmpty()){
            if(newPass.text.toString().equals(confirmPass.text.toString())){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Warning!")

                builder.setMessage("Press Yes to Confirm")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                builder.setPositiveButton("Yes"){ dialogInterface, which ->
                    val newPassword = newPass.text.toString()

                    firebaseUser!!.updatePassword(newPassword).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                builder.setNeutralButton("Cancel"){ dialogInterface, which ->
                    alertDialog.dismiss()
                }
                alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            else{
                Toast.makeText(this, "Password Doesn't Match", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Please fill the fields", Toast.LENGTH_SHORT).show()
        }
    }
}