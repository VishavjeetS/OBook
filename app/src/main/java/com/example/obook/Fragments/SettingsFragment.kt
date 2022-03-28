package com.example.obook.Fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.*
import com.example.obook.R
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.*

import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.obook.R.id.help_item
import com.example.obook.Welcome
import com.example.obook.nameChange
import com.example.obook.passChange
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater


class SettingsFragment : Fragment() {
    lateinit var listView:ListView
    lateinit var al : ArrayList<String>
    lateinit var aa:ArrayAdapter<String>
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
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageUri = it
                Log.d(TAG, it.toString())
                imgV.setImageURI(it)
            }
        )

        imagc = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == AppCompatActivity.RESULT_OK){
                val bm=it.data?.extras?.get("data") as Bitmap
                imgV.setImageBitmap(bm)
            }
        }

        listView = view.findViewById(R.id.list)
        al = ArrayList()
        aa = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_list_item_1, al)
        listView.adapter = aa
        al.add("Change Name")
        al.add("Change Password")
        al.add("Change Profile Photo")
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
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Warning!")

                    builder.setMessage("Do you want to exit?")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    builder.setPositiveButton("Choose From Gallery"){ dialogInterface, which ->
                        getImage.launch("image/*")
                    }
                    builder.setNegativeButton("Open Camera"){dialogInterface, which->
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        imagc.launch(intent)
                    }
                    builder.setNeutralButton("Cancel"){ dialogInterface, which ->
                        alertDialog.dismiss()
                    }
                    alertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                al[3] -> {
                    val Intent = Intent(view.context, Welcome::class.java)
                    startActivity(Intent)
                }
            }
        }
    }
}