package com.example.obook.model.animeModel.Manga.jinka


import com.google.gson.annotations.SerializedName

data class External(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)