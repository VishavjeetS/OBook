package com.example.obook.model.CastModel

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