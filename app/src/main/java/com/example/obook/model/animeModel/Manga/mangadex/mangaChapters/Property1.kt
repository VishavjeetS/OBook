package com.example.obook.model.animeModel.Manga.mangadex.mangaChapters


import com.google.gson.annotations.SerializedName

data class Property1(
    @SerializedName("chapters")
    val chapters: Chapters,
    @SerializedName("count")
    val count: Int,
    @SerializedName("volume")
    val volume: String
)