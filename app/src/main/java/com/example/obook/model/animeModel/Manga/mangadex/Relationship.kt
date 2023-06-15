package com.example.obook.model.animeModel.Manga.mangadex


import com.google.gson.annotations.SerializedName

data class Relationship(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String
)