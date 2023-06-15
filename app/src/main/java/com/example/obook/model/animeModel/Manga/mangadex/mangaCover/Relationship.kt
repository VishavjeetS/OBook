package com.example.obook.model.animeModel.Manga.mangadex.mangaCover


import com.google.gson.annotations.SerializedName

data class Relationship(
    @SerializedName("attributes")
    val attributes: AttributesX,
    @SerializedName("id")
    val id: String,
    @SerializedName("related")
    val related: String,
    @SerializedName("type")
    val type: String
)