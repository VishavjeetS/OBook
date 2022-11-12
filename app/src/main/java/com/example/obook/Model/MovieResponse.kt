package com.example.obook.Model

import android.os.Parcelable
import com.example.obook.Model.Movies
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(
    @SerializedName("results")
    val movies: List<Movies>,
    val site: String
):Parcelable{
    constructor() : this(mutableListOf(), "")
}