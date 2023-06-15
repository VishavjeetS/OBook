package com.example.obook.model.animeModel.Manga.mangadex


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("id")
    val id: String,
    @SerializedName("relationships")
    val relationships: List<Relationship>,
    @SerializedName("type")
    val type: String
)