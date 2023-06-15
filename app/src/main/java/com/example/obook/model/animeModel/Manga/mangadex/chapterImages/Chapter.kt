package com.example.obook.model.animeModel.Manga.mangadex.chapterImages


import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("data")
    val `data`: List<String>,
    @SerializedName("dataSaver")
    val dataSaver: List<String>,
    @SerializedName("hash")
    val hash: String
)