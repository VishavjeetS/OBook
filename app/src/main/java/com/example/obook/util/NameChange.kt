package com.example.obook.util

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.obook.Model.User
import com.example.obook.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NameChange : Fragment() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_name_change, container, false)
        val newName = view.findViewById<EditText>(R.id.name_update1)
        database = Firebase.database.reference
        val userObj = User.getInstance()
        val userId = userObj.getUID()

        println("userName: " + userId)

        val provider = Constant().getProviderId()
        print("provider: " + provider)

        val changeName = view.findViewById<Button>(R.id.changename)
        changeName.setOnClickListener {
            val name = newName.text.toString()
            val firebaseUserID = FirebaseAuth.getInstance().currentUser?.uid!!
            val constObj = Constant.getInstance()
            if(constObj.getProviderId()=="password"){
                database = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                database.addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userMap = HashMap<String, Any>()
                        userMap["name"] = name
                        userMap["email"] = userObj.getEmail().toString()
                        userMap["uid"] = userObj.getUID()
                        if(snapshot.exists()){
                            database.updateChildren(userMap).addOnSuccessListener {
                                Toast.makeText(requireContext(), "Name Updated + $userMap + ${userObj.getEmail()}", Toast.LENGTH_SHORT).show()
                                userObj.setName(name)
                                view.visibility = View.GONE
                                Log.d("Success ", "$userMap + ${userObj.getEmail()}")
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                                Log.d("Failed Name ", it.message.toString())
                            }
                        }
                        else{
                            database.setValue(userMap).addOnSuccessListener {
                                Toast.makeText(requireContext(), "Added to database", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Log.d("Failed Adding ", it.message.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
            else{
                val user = Firebase.auth.currentUser
                val name = newName.text.toString()
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            userObj.setName(name)
                            view.visibility = View.GONE
                            Log.d("GName", "${userObj.getName()} ${user.displayName}")
                        }
                    }
            }
        }
        return view
    }

}