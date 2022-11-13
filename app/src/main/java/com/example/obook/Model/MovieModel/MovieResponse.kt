package com.example.obook.Model.MovieModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MovieResponse(
    @SerializedName("results")
    val movies: List<Movies>,
):Parcelable{
    constructor() : this(mutableListOf())
}