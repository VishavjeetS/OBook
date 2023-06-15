package com.example.obook.model.animeModel.Manga.mangadex.mangaList


import com.google.gson.annotations.SerializedName

data class RelationshipX(
    @SerializedName("id")
    val id: String,
    @SerializedName("related")
    val related: String,
    @SerializedName("type")
    val type: String
)