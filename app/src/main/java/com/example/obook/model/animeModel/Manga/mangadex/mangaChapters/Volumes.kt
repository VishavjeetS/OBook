package com.example.obook.model.animeModel.Manga.mangadex.mangaChapters


import com.google.gson.annotations.SerializedName

data class Volumes(
    @SerializedName("chapters")
    val chapters: Map<String, Chapters>,
    @SerializedName("count")
    val count: Int,
    @SerializedName("volume")
    val volume: String
)