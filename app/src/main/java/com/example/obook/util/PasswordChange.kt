package com.example.obook.util

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.obook.MainActivity
import com.example.obook.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class PasswordChange : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        val view = inflater.inflate(R.layout.fragment_password_change, container, false)
        val updatePass = view.findViewById<Button>(R.id.updatePass)
        val newPass = view.findViewById<EditText>(R.id.new_pass)
        val confirmPass = view.findViewById<EditText>(R.id.confirm_pass)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        updatePass.setOnClickListener {
            val newText = newPass.text
            val confirmText = confirmPass.text
            if(newText.equals(confirmText)){
                val newPassword = newPass.text.toString()

                firebaseUser.updatePassword(newPassword).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(requireContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }
                    else{
                        Toast.makeText(requireContext(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "Password doesn't match", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}