package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("id")
    val id: String,
    @SerializedName("relationships")
    val relationships: List<RelationshipX>,
    @SerializedName("type")
    val type: String
)