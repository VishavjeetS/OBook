package com.example.obook.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.obook.R
import java.util.ArrayList
import android.widget.ListView
import android.widget.ArrayAdapter
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.fragment_book.*
import android.content.Intent.getIntent
import com.example.obook.Details


class BookFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_book, container, false)
//        val value = requireArguments().getSerializable("Key") as Details
//        movieName1.text = value.movie
//        showTime.text = value.time
        return v
    }
}