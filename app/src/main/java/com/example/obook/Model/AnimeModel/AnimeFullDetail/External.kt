package com.example.obook.Model.AnimeModel.AnimeFullDetail


import com.google.gson.annotations.SerializedName

data class External(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)