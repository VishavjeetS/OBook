package com.example.obook.model.animeModel.anime.animeFullDetail


import com.google.gson.annotations.SerializedName

data class Streaming(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)