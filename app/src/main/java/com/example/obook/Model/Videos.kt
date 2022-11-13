package com.example.obook.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Videos(
    @PrimaryKey
    @SerializedName("id")
    val id : String,
    @SerializedName("site")
    val site: String?,
    @SerializedName("key")
    val key:String?,
    @SerializedName("official")
    val official:Boolean,
): Parcelable {
    constructor() : this("", "", "", false)
}