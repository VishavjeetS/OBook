package com.example.obook.Model

import android.os.Parcelable
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
class AnimeResponse (
    @SerializedName("data")
    val data: List<Anime>
    ): Parcelable