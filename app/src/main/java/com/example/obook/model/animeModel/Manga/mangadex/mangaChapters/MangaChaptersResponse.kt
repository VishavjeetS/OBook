package com.example.obook.model.animeModel.Manga.mangadex.mangaChapters


import com.google.gson.annotations.SerializedName

data class MangaChaptersResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("volumes")
    val volumes: Map<String, Volumes>
)