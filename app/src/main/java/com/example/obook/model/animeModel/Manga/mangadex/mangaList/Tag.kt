package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("attributes")
    val attributes: AttributesX,
    @SerializedName("id")
    val id: String,
    @SerializedName("relationships")
    val relationships: List<Relationship>,
    @SerializedName("type")
    val type: String
)