package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class Description(
    @SerializedName("property1")
    val property1: String,
    @SerializedName("property2")
    val property2: String
)