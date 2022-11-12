package com.example.obook.util

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.obook.R
import com.example.obook.Authentication.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassword : Fragment() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        val email1 = view.findViewById<EditText>(R.id.passEmail)
        val button = view.findViewById<Button>(R.id.verificationBtn)

        mAuth = Firebase.auth

        button.setOnClickListener {
            val email = email1!!.text.toString()
            if (!TextUtils.isEmpty(email)) {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val message = "Email sent."
                            Log.d(TAG, message)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else {
                            Log.w(TAG, task.exception?.message!!)
                            Toast.makeText(requireContext(), "No user found with this email.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Enter Email", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun updateUI() {
        startActivity(Intent(requireContext(), Login::class.java))
        activity?.finish()
    }

}