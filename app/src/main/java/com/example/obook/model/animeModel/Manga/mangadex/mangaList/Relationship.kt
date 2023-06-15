package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class Relationship(
    @SerializedName("attributes")
    val attributes: AttributesXX,
    @SerializedName("id")
    val id: String,
    @SerializedName("related")
    val related: String,
    @SerializedName("type")
    val type: String
)