package com.example.obook.Model.MovieModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MovieResponse(
    @SerializedName("results")
    val movies: List<Movies>,
    @SerializedName("total_pages")
    val tp: String
):Parcelable{
    constructor() : this(mutableListOf(), "")
}