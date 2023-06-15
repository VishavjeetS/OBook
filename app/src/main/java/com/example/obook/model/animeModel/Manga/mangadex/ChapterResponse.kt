package com.example.obook.model.animeModel.Manga.mangadex


import com.google.gson.annotations.SerializedName

data class ChapterResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("response")
    val response: String,
    @SerializedName("result")
    val result: String
)