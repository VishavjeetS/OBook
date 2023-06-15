package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class AttributesX(
    @SerializedName("description")
    val description: Description,
    @SerializedName("group")
    val group: String,
    @SerializedName("name")
    val name: Name,
    @SerializedName("version")
    val version: Int
)