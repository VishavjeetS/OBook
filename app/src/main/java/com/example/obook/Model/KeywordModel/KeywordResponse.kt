package com.example.obook.Model.KeywordModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeywordResponse(
    @SerializedName("keywords")
    val keywords: List<Keywords>
):Parcelable{
    constructor(): this(mutableListOf())
}