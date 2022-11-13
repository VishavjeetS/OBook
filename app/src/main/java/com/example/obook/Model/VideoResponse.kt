package com.example.obook.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class VideoResponse (
    @SerializedName("results")
    val videos: List<Videos>
    ): Parcelable{
        constructor(): this(mutableListOf())
    }