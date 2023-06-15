package com.example.obook.model.animeModel.Manga.mangadex.chapterImages


import com.google.gson.annotations.SerializedName

data class ChapterImagesResponse(
    @SerializedName("baseUrl")
    val baseUrl: String,
    @SerializedName("chapter")
    val chapter: Chapter,
    @SerializedName("result")
    val result: String
)