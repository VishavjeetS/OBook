package com.example.obook.Model.TvModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TvResponse (
    @SerializedName("results")
    val result: List<TV>,
    @SerializedName("total_pages")
    val tp: String
        ): Parcelable{
            constructor() : this(mutableListOf(), "")
        }