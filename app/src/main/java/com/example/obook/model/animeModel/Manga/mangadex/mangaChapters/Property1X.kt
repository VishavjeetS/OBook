package com.example.obook.model.animeModel.Manga.mangadex.mangaChapters


import com.google.gson.annotations.SerializedName

data class Property1X(
    @SerializedName("chapter")
    val chapter: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("others")
    val others: List<String>
)