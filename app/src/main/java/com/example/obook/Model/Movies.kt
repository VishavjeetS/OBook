package com.example.obook.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movies(
    @SerializedName("id")
    val id : String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_count")
    val vote_count:String?,
    @SerializedName("overview")
    val overview : String?,
    @SerializedName("poster_path")
    val poster_path:String?,
    @SerializedName("release_date")
    val release_date:String
):Parcelable{
    constructor() : this("", "", "","","", "")
}