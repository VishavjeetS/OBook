package com.example.obook.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.obook.Details
import com.example.obook.R
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_browse.view.*







class BrowseFragment : Fragment() {
    lateinit var alertDialog: AlertDialog
    var date = ""
    var time = ""
    var movie = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_browse, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        date1.setOnClickListener { date = "25" }
        date2.setOnClickListener { date = "26" }
        date3.setOnClickListener { date = "27" }
        date4.setOnClickListener { date = "28" }
        date5.setOnClickListener { date = "29" }
        date6.setOnClickListener { date = "30" }

        time1.setOnClickListener { time = "11:00 AM" }
        time2.setOnClickListener { time = "01:30 PM" }
        time3.setOnClickListener { time = "04:00 PM" }

        mov1.setOnClickListener { movie = "World of Warcraft" }
        mov2.setOnClickListener { movie = "Kong: Skull Island" }
        mov3.setOnClickListener { movie = "The Lego Batman" }
        mov4.setOnClickListener { movie = "World of Warcraft" }

        var array = arrayOf(date, time, movie)

        val obj = Details(date, time, movie)



        proceed.setOnClickListener {
            obj.date = date
            obj.movie = movie
            obj.time = time
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Warning!")

            builder.setMessage("Do you want to book ticket? Movie: $movie, Date: $date and Time: $time")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Yes") { dialogInterface, which ->
//                val ldf = BookFragment()
//                val args = Bundle()
//                args.putSerializable("Key", obj)
//                ldf.setArguments(args)
//
//                requireFragmentManager().beginTransaction().add(R.id.container, ldf).commit()
                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                alertDialog.dismiss()
            }

            alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}