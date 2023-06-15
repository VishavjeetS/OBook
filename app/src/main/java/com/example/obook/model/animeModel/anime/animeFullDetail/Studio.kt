package com.example.obook.model.animeModel.anime.animeFullDetail


import com.google.gson.annotations.SerializedName

data class Studio(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)