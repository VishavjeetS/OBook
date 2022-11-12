package com.example.obook.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.obook.R
import android.content.Intent
import android.net.Uri
import android.widget.*

import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.obook.Authentication.Login
import com.example.obook.util.NameChange
import com.example.obook.util.PasswordChange
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {
    lateinit var listView:ListView
    lateinit var al : ArrayList<String>
    lateinit var aa:ArrayAdapter<String>
    lateinit var mauth: FirebaseAuth
    lateinit var alertDialog:AlertDialog
    lateinit var imageUri: Uri
    lateinit var imagc : ActivityResultLauncher<Intent>
//    lateinit var imgV:ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
//        imgV = v.findViewById(R.id.imgV)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mauth = FirebaseAuth.getInstance()

        listView = view.findViewById(R.id.list)
        al = ArrayList()
        aa = ArrayAdapter<String>(requireActivity(),R.layout.list_white_items, al)
        listView.adapter = aa
        al.add("Change Name")
        al.add("Change Password")
        al.add("Log Out")
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            when(al[position]){
                al[0] -> {
                    makeCurrentScreen(NameChange())
                }
                al[1] -> {
                    makeCurrentScreen(PasswordChange())
                }
                al[2] -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), Login::class.java))
                    Toast.makeText(requireContext(),"Log out", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }
    }
    private fun makeCurrentScreen(fragment: Fragment) = activity?.supportFragmentManager!!.beginTransaction().apply {
        replace(R.id.frameLayout, fragment)
        this.commit()
    }
}