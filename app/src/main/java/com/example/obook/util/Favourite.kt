package com.example.obook.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import com.example.obook.R
import kotlinx.android.synthetic.main.fragment_favourite.*

class Favourite : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        val signInfo = Constant().getInfo()
        val tv = view.findViewById<TextView>(R.id.tv)
        println(tv)
        if(!signInfo){
            tv!!.visibility = View.VISIBLE
        }
        return view
    }

}