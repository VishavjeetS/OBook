package com.example.obook.Model.KeywordModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Keywords(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
):Parcelable{
    constructor():this("", "")
}