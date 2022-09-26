package com.example.obook.util

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.obook.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.*

import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.obook.Welcome
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


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
                    val Intent = Intent(view.context, nameChange::class.java)
                    startActivity(Intent)
                }
                al[1] -> {
                    val Intent = Intent(view.context, passChange::class.java)
                    startActivity(Intent)
                }
                al[2] -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), Welcome::class.java))
                    Toast.makeText(requireContext(),"Log out", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }
    }
}