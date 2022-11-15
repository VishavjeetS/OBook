package com.example.obook.Model.CastModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CastResponse (
    @SerializedName("cast")
    val userList: List<Cast>
        ):Parcelable{
            constructor(): this(mutableListOf())
        }