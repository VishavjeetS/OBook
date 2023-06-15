package com.example.obook.model.animeModel.Manga.mangadex.mangaCover


import com.google.gson.annotations.SerializedName

data class MangaCover(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("response")
    val response: String,
    @SerializedName("result")
    val result: String
)