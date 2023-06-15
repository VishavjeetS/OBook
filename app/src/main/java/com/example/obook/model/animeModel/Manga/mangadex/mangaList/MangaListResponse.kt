package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class MangaListResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("response")
    val response: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("total")
    val total: Int
)