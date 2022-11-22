package com.example.obook.Model.CastModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cast (
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val userId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val image: String?,
    @SerializedName("credit_id")
    val credit_id: String,
    @SerializedName("character")
    val character: String,
        ): Parcelable{
            constructor(): this(0, 0, "", "", "", "")
        }