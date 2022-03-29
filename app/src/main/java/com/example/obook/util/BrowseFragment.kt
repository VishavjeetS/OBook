package com.example.obook.util

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obook.Adapter.MovieAdapter
import com.example.obook.Model.MovieResponse
import com.example.obook.Model.Movies
import com.example.obook.R
import com.example.obook.detailActivity
import com.example.obook.services.MovieApiInterface
import com.example.obook.services.MovieApiService
import kotlinx.android.synthetic.main.fragment_browse.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class BrowseFragment : Fragment() {
    private lateinit var listView:ListView
    private lateinit var optionList : ArrayList<String>
    lateinit var optionAdapter: ArrayAdapter<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_browse, container, false)
        listView = v.findViewById(R.id.browseList)
        optionList = ArrayList()
        optionAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, optionList)
        listView.adapter = optionAdapter
        optionList.add("Popular Movies")
        optionList.add("Top Rated")
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                when (optionList[position]) {
                    optionList[0] -> {
                        frameLayout.visibility = View.VISIBLE
                        parentFragmentManager.beginTransaction().replace(R.id.frameLayout, Popular()).commit()
                    }
                    optionList[1] -> {
                        frameLayout.visibility = View.VISIBLE
                        parentFragmentManager.beginTransaction().replace(R.id.frameLayout, TopRated()).commit()
                    }
                }
            }
        return v
    }

}